package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FrameBufferObject {

	private int FboID;
	private int textureID;
	private int depthTextureID;
	private int depthBufferID;
	private int width;
	private int height;

	public FrameBufferObject(int width, int height, boolean depthTexture) {
		this.width = width;
		this.height = height;

		makeFBO();
		makeTextureAttachment();

		if (!depthTexture) {
			makeDepthBufferAttachment();
		} else {
			makeDepthTextureAttachment();
		}

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("FBO Error");
		}

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	private void makeFBO() {
		FboID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, FboID);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
	}

	private void makeTextureAttachment() {
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureID, 0);

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	private void makeDepthTextureAttachment() {
		depthTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, depthTextureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTextureID, 0);
	}

	private void makeDepthBufferAttachment() {
		depthBufferID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID);
	}

	public void activate() {
		glBindFramebuffer(GL_FRAMEBUFFER, FboID);
		glViewport(0, 0, width, height);
	}

	public int getID() {
		return FboID;
	}

	public int getTexture() {
		return textureID;
	}

	public int getDepthTexture() {
		return depthTextureID;
	}
}
