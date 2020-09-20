package jogo;

import java.util.Random;

public class GameCore {
	
	protected int width = 20;
	protected int height = 20;
	
	public static final char MUSHROM = 'm';
	public static final char WALL = 'w';
	public static final char WAY = 'o';
	public static final char START = 's';
	public static final char TARGET = 't';
	
	public static final int RASTER = 10;
	
	protected int maxdepth = 0;
	protected int depth = 0;
	protected int lx = 0;
	protected int ly = 0;
	protected char[][] labyMap = null;
	
	Random rd = new Random(System.currentTimeMillis());
	
	public boolean isValidMove(int x, int y) {
		try {
			if ((labyMap[y][x] == WAY) || (labyMap[y][x] == MUSHROM)) {
				return true;
			}

		} catch (ArrayIndexOutOfBoundsException e) {
		}

		return false;
	}
	
	protected void drawMap(int largura, int altura, int xpos, int ypos) {
		int dir_check = 0;
		labyMap[ypos][xpos] = WAY;

		if (maxdepth == depth) {
			if (xpos == 1) {
				lx = xpos - 1;
				ly = ypos;
			} else if (xpos == largura - 2) {
				lx = xpos + 1;
				ly = ypos;
			} else if (ypos == 1) {
				lx = xpos;
				ly = ypos - 1;
			} else if (ypos == altura - 2) {
				lx = xpos;
				ly = ypos + 1;
			}
		}

		depth++;
		if (depth > maxdepth)
			maxdepth = depth;

		while (dir_check != 1 + 2 + 4 + 8) {
			switch (rd.nextInt() % 4) {
			case 0: // acima
				if (!valid(largura, altura, xpos, ypos - 1)
						|| !valid(largura, altura, xpos - 1, ypos - 1)
						|| !valid(largura, altura, xpos + 1, ypos - 1)
						|| !valid(largura, altura, xpos, ypos - 2)) {
					dir_check |= 1;
					continue;
				}
				drawMap(largura, altura, xpos, ypos - 1);
				break;

			case 1: // embaixo
				if (!valid(largura, altura, xpos, ypos + 1)
						|| !valid(largura, altura, xpos - 1, ypos + 1)
						|| !valid(largura, altura, xpos + 1, ypos + 1)
						|| !valid(largura, altura, xpos, ypos + 2)) // oben
				{
					dir_check |= 2;
					continue;
				}
				drawMap(largura, altura, xpos, ypos + 1);
				break;

			case 2: // direita
				if (!valid(largura, altura, xpos + 1, ypos)
						|| !valid(largura, altura, xpos + 1, ypos - 1)
						|| !valid(largura, altura, xpos + 2, ypos)
						|| !valid(largura, altura, xpos + 1, ypos + 1)) {
					dir_check |= 4;
					continue;
				}
				drawMap(largura, altura, xpos + 1, ypos);
				break;

			case 3: // esquerda
				if (!valid(largura, altura, xpos - 1, ypos)
						|| !valid(largura, altura, xpos - 1, ypos - 1)
						|| !valid(largura, altura, xpos - 2, ypos)
						|| !valid(largura, altura, xpos - 1, ypos + 1)) {
					dir_check |= 8;
					continue;
				}
				drawMap(largura, altura, xpos - 1, ypos);
				break;
			}
		}
		depth--;
		return;
	}

	public boolean valid(int breite, int hoehe, int xpos, int ypos) {
		if (xpos < 0 || xpos >= breite || ypos < 0 || ypos >= hoehe
				|| this.labyMap[ypos][xpos] == WAY
				|| this.labyMap[ypos][xpos] == MUSHROM) {
			return false;
		}
		return true;
	}

	public char[][] getLabyMap() {
		return labyMap;
	}
}
