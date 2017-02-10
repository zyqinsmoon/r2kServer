package com.apabi.r2k.common.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apabi.r2k.common.utils.PropertiesUtil;

/**
 * 验证码生成器.
 */
public class ValidateCodeGenerator extends HttpServlet {
	private static final int MAX_CODE_LENGTH = 255;

	private static final long serialVersionUID = 174481365032769155L;

	private static final char[] SEEDS = PropertiesUtil.get("randseeds").toCharArray();
	private static final int DEFAULT_WIDTH = 60;
	private static final int DEFAULT_HEIGHT = 20;

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		final int width = PropertiesUtil.getInt("_validatecode_width", DEFAULT_WIDTH);
		final int height = PropertiesUtil.getInt("_validatecode_height", DEFAULT_HEIGHT);

		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "No-cache");
		resp.setDateHeader("Expires", 0);

		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics graphics = image.getGraphics();
		final Random random = new Random();
		graphics.setColor(makeRandomColor(200, 255));
		graphics.fillRect(0, 0, width, height);

		graphics.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		graphics.setColor(makeRandomColor(160, 200));
		for (int i = 0; i < 150; i++) {
			final int x = random.nextInt(width);
			final int y = random.nextInt(height);

			final int x1 = random.nextInt(12);
			final int y1 = random.nextInt(12);
			graphics.drawLine(x, y, x + x1, y + y1);
		}

		final StringBuilder sRand = new StringBuilder();
		String rand = "";
		for (int i = 0; i < 4; i++) {
			rand = SEEDS[random.nextInt(SEEDS.length)] + "";
			sRand.append(rand);
			graphics.setColor(new Color(20 + random.nextInt(110), 
					20 + random.nextInt(110), 20 + random.nextInt(110)));
			graphics.drawString(rand, 13 * i + 6, 16);
		}
		final HttpSession session = req.getSession();
		session.setAttribute("_validate_code", sRand.toString());
		graphics.dispose();
		ImageIO.write(image, "JPEG", resp.getOutputStream());
	}

	private Color makeRandomColor(int start, int end) {
		final Random random = new Random();
		if (start > MAX_CODE_LENGTH) {
			start = MAX_CODE_LENGTH;
		}
		if (end > MAX_CODE_LENGTH) {
			end = MAX_CODE_LENGTH;
		}

		final int r = start + random.nextInt(end - start);
		final int g = start + random.nextInt(end - start);
		final int b = start + random.nextInt(end - start);

		return new Color(r, g, b);
	}
}
