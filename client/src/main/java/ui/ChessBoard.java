package ui;

public class ChessBoard {
    private String[][] squares = new String[8][8];

    public void ChessBoardWhite() {
        chessPieces();
        colLettersWhite();
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1 + " "));
            boardColors(row);
        }
    }

    public void ChessBoardBlack() {
        chessPieces();
        colLettersBlack();
        for (int row = 0; row <= 7; row++) {
            System.out.print((row + 1 + " "));
            boardColors(row);
        }
    }

    private void colLettersWhite() {
        for (int col = 1; col <= 8; col++) {
            if (col == 1) {
                System.out.print(("   a "));
            } else if (col == 2) {
                System.out.print(" b ");
            } else if (col == 3) {
                System.out.print(" c ");
            } else if (col == 4) {
                System.out.print(" d ");
            } else if (col == 5) {
                System.out.print(" e ");
            } else if (col == 6) {
                System.out.print(" f ");
            } else if (col == 7) {
                System.out.print(" g ");
            } else if (col == 8) {
                System.out.print(" h ");
            }
        }
        System.out.println();
    }

    private void colLettersBlack() {
        for (int col = 8; col >= 1; col--) {
            if (col == 8) {
                System.out.print(("   h "));
            } else if (col == 7) {
                System.out.print(" g ");
            } else if (col == 6) {
                System.out.print(" f ");
            } else if (col == 5) {
                System.out.print(" e ");
            } else if (col == 4) {
                System.out.print(" d ");
            } else if (col == 3) {
                System.out.print(" c ");
            } else if (col == 2) {
                System.out.print(" b ");
            } else if (col == 1) {
                System.out.print(" a ");
            }
        }
        System.out.println();
    }

    private void boardColors(int row) {
        for (int col = 0; col < 8; col++) {
            boolean evenRow = (row % 2) == 0;
            boolean evenCol = (col % 2) == 0;
            boolean oddRow = (row % 2) != 0;
            boolean oddCol = (col % 2) != 0;
            String squareColor = EscapeSequences.SET_BG_COLOR_DARK_GREY;
            if (evenRow == evenCol) {
                squareColor = EscapeSequences.SET_BG_COLOR_DARK_GREY;
            }
            if (oddRow == oddCol) {
                squareColor = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
            }
            String piece = squares[row][col];
            if (piece == null) {
                piece = "   ";
            }
            System.out.print(squareColor + piece);
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }

    public void chessPieces() {
        for (int col = 0; col < 8; col++) {
            squares[1][col] = EscapeSequences.WHITE_PAWN;
        }
        squares[0][0] = EscapeSequences.WHITE_ROOK;
        squares[0][1] = EscapeSequences.WHITE_KNIGHT;
        squares[0][2] = EscapeSequences.WHITE_BISHOP;
        squares[0][3] = EscapeSequences.WHITE_QUEEN;
        squares[0][4] = EscapeSequences.WHITE_KING;
        squares[0][5] = EscapeSequences.WHITE_BISHOP;
        squares[0][6] = EscapeSequences.WHITE_KNIGHT;
        squares[0][7] = EscapeSequences.WHITE_ROOK;

        for (int col = 0; col < 8; col++) {
            squares[6][col] = EscapeSequences.BLACK_PAWN;
        }
        squares[7][0] = EscapeSequences.BLACK_ROOK;
        squares[7][1] = EscapeSequences.BLACK_KNIGHT;
        squares[7][2] = EscapeSequences.BLACK_BISHOP;
        squares[7][3] = EscapeSequences.BLACK_QUEEN;
        squares[7][4] = EscapeSequences.BLACK_KING;
        squares[7][5] = EscapeSequences.BLACK_BISHOP;
        squares[7][6] = EscapeSequences.BLACK_KNIGHT;
        squares[7][7] = EscapeSequences.BLACK_ROOK;
    }
}






