public class Utility {

  public static void main(String[] args) {
    char[][] board = {{'-', '-', '-', '-', '-', '-', '-', '-'},
                      {'-', '-', '-', '-', '-', '-', '-', '-'},
                      {'-', '-', '-', '-', 'X', '-', '-', '-'},
                      {'-', '-', 'O', 'X', 'X', '-', '-', '-'},
                      {'-', '-', '-', 'O', 'X', '-', '-', '-'},
                      {'-', '-', '-', '-', 'O', '-', '-', '-'},
                      {'-', '-', '-', '-', '-', '-', '-', '-'},
                      {'-', '-', '-', '-', '-', '-', '-', '-'}};
    char[][] newboard = {{'-','O','O','-','O'}};
    System.out.println(UtilityFunction(board,'X'));
  }

  //TODO: evaluate something like {'-','O','O','-','O'} ?
  public static int UtilityFunction(char[][] state, char playerPiece){
    int score = 0;
    boolean foundSingle = false;
    boolean foundDouble = false;
    boolean foundTriple = false;
    boolean leftBlocked = false;
    boolean rightBlocked = false;
    boolean topBlocked = false;
    boolean bottomBlocked = false;
    boolean pairGap = false;
    boolean singleGap = false;
    //triple count for killmove; if >=2, opponent cannot stop win
    int tripleCount = 0;

    //eval rows
    for(int row = 0; row < state.length; row++) {
      for (int column = 0; column < state[0].length; column++) {
        //if not player piece (is - or opponent)
        if(!(state[row][column] == playerPiece)) {
          score += evaluate(foundSingle, foundDouble, foundTriple, leftBlocked, rightBlocked, singleGap, pairGap );
          if( state[row][column] == '-' ){
            pairGap = false;
            singleGap = false;
            if( foundDouble ){
              pairGap = true;
            }
            if( foundSingle ){
              singleGap = true;
            }
          }
          else{
            //opponentpiece
            pairGap = false;
            singleGap = false;
          }
          foundSingle = false;
          foundDouble = false;
          foundTriple = false;
          leftBlocked = false;
          rightBlocked = false;
          continue;
        }

        //if first player piece found
        if(!foundSingle) {
          foundSingle = true;
          //if within bounds of array
          if((column - 1) > -1) {
            //check left blocked
            if(state[row][column-1] != '-') {
              leftBlocked = true;
            }
          } else {
            //end of array, left blocked
            leftBlocked = true;
          }
          continue;
        }

        //if second player piece found
        if(!foundDouble) {
          foundDouble = true;
          //if right blocked
          if((column + 1) < state[0].length) {
            if(state[row][column+1] != '-' && state[row][column+1] != playerPiece) {
              rightBlocked = true;
            }
          } else {
            //end of array, right blocked
            rightBlocked = true;
          }
          continue;
        }

        //if triple found
        if(!foundTriple) {
          foundTriple = true;
          if((column + 1) < state[0].length) {
            if(state[row][column+1] != '-' && state[row][column+1] != playerPiece) {
              rightBlocked = true;
            }
          } else {
            //end of array, right blocked
            rightBlocked = true;
          }
          //if triple unblocked on one/both sides, add to triple count for killmoves
          if( ( !rightBlocked && !leftBlocked ) ||
              ( !rightBlocked && leftBlocked )||
              ( rightBlocked && !leftBlocked) ) {
            tripleCount++;
          }
          continue;
        }

        //if here, four in a row, win found
        return 20;
      }
      //end of row
      rightBlocked = true;
      score += evaluate(foundSingle, foundDouble, foundTriple, leftBlocked, rightBlocked, singleGap, pairGap);

      foundSingle = false;
      foundDouble = false;
      foundTriple = false;
      leftBlocked = false;
      rightBlocked = false;
      singleGap = false;
      pairGap = false;
    }

    //eval columns
    for (int column = 0; column < state[0].length; column++) {
      for(int row = 0; row < state.length; row++) {
        //if not player piece (is - or opponent)
        if(!(state[row][column] == playerPiece)) {
          score += evaluate(foundSingle, foundDouble, foundTriple, topBlocked, bottomBlocked, singleGap, pairGap );
          if( state[row][column] == '-' ){
            pairGap = false;
            singleGap = false;
            if( foundDouble ){
              pairGap = true;
            }
            if( foundSingle ){
              singleGap = true;
            }
          }
          else{
            //opponentpiece
            pairGap = false;
            singleGap = false;
          }
          foundSingle = false;
          foundDouble = false;
          foundTriple = false;
          topBlocked = false;
          bottomBlocked = false;
          continue;
        }

        //if first player piece found
        if(!foundSingle) {
          foundSingle = true;

          //if within bounds of array
          if((row - 1) > -1) {
            //check top blocked
            if(state[row-1][column] != '-') {
              topBlocked = true;
            }
          } else {
            //end of array
            topBlocked = true;
          }
          continue;
        }

        //if second player piece found
        if(!foundDouble) {
          foundDouble = true;

          //if bottom blocked
          if((row + 1) < state.length) {
            if(state[row + 1][column] != '-' && state[row + 1][column] != playerPiece) {
              bottomBlocked = true;
            }
          } else {
            //end of array, bottom blocked
            bottomBlocked = true;
          }

          continue;
        }

        //if triple found
        if(!foundTriple) {
          foundTriple = true;

          //if bottom blocked
          if((row + 1) < state.length) {
            if(state[row + 1][column] != '-' && state[row + 1][column] != playerPiece) {
              bottomBlocked = true;
            }
          } else {
            //end of array, bottom blocked
            bottomBlocked = true;
          }

          //if triple unblocked on one/both sides, add to triple count for killmoves
          if( ( !bottomBlocked && !topBlocked ) ||
              ( bottomBlocked && !topBlocked ) ||
              ( !bottomBlocked && topBlocked) ){
            tripleCount++;
          }

          continue;
        }

        //if here, four in a row, win found
        return 20;
      }
      //end of column
      bottomBlocked = true;
      score += evaluate(foundSingle, foundDouble, foundTriple, topBlocked, bottomBlocked, singleGap, pairGap);
      foundSingle = false;
      foundDouble = false;
      foundTriple = false;
      topBlocked = false;
      bottomBlocked = false;
      singleGap = false;
      pairGap = false;
    }

    if(tripleCount >= 2) {
      return 20;
    }
    return score;
  }

  private static int evaluate(boolean foundSingle, boolean foundDouble, boolean foundTriple, boolean leftOrTopBlocked, boolean rightOrBottomBlocked, boolean singleGap, boolean pairGap ) {
    int score = 0;
    //return highest because if triple = true, double & single = true

    //triple w/ 2 unblocked
    if (foundTriple && !leftOrTopBlocked && !rightOrBottomBlocked) {
      score += 20;
      return score;
    }

    //triple w/ 1 option
    if ((foundTriple && leftOrTopBlocked && !rightOrBottomBlocked) ||
        (foundTriple && !leftOrTopBlocked && rightOrBottomBlocked)) {
      score += 3;
      return score;
    }

    //pair w/ 2 option
    if ((foundDouble && !leftOrTopBlocked && !rightOrBottomBlocked))  {
      if(singleGap || pairGap){
        score += 3;
      }
      score += 2;
      return score;
    }

    //pair w/ 1 option
    if ((foundDouble && leftOrTopBlocked && !rightOrBottomBlocked) ||
        (foundDouble && rightOrBottomBlocked && !leftOrTopBlocked)) {
      score += 1;
      return score;
    }

    if(foundSingle && pairGap){
      score += 3;
      return score;
    }

    //if here, score = 0
    return score;
  }
}
