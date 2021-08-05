package br.com.lucasrocha.syntaticanalyzer;

import java.util.Stack;
import java.util.LinkedList;
import java.io.FileReader;
import java.io.IOException;

public class langParser2 {
    private LextTest lx;
    private Stack<Token> stk;
    private LinkedList<Token> bck;
    private Token nxt;
    private boolean eof;
    
    // Depuracao
    private String idnt;
    private void incS(){ idnt += "   ";}
    private void decS(){ idnt = idnt.substring(0,idnt.length() - 3);}
    private void msg(String s) {
        System.out.println(idnt + s + " [>|" + nxt + " ]");
    }
    
    public langParser2(String fileName) throws IOException{
        lx = new LextTest(new FileReader(fileName));
        stk = new Stack<Token>();
        bck = new LinkedList<Token>();
        nxt = lx.nextToken();
        eof = nxt == null;
        idnt = "";
    }
    
    private void readToken() throws IOException{
        if(!eof){
            stk.push(nxt);
            if(bck.isEmpty()){
                nxt = lx.nextToken();
                eof = nxt == null;
            }else{
                nxt = bck.remove();
            }
        }
    }
    
    private void backtrack(int n) throws IOException{
        if(n > 0 && nxt == null){ nxt = stk.pop(); n--;}
        while(n > 0){
            bck.addFirst(nxt);
            nxt = stk.pop();
            n--;
        }
        if(!bck.isEmpty()){ eof = false;}
    }
    
    private boolean match(TOKEN_TYPE t) throws IOException {
        if(nxt != null && nxt.t == t){
            readToken();
            msg("(*) Consumed " + stk.peek() + " EOF:" + eof);
            return true;
        }
        return false;
    }
    
    public boolean prog() throws IOException{
        msg("prog");
        incS();
        boolean result = stmtList() && eof;
        System.out.println("Last: " + nxt);
        System.out.println("Remaining input: ");
        while(nxt != null){
            System.out.println("" + nxt);
            readToken();
        }
        System.out.println("EOF status: " + eof);
        return result;
    }
    
    public boolean stmtList() throws IOException{
        int n = stk.size();
        msg("stmtList -> stmt ; StmtList");
        incS();
        if(stmt()){
            if(match(TOKEN_TYPE.SEMI)){
                if(stmtList()){
                    decS();
                    msg("[ok]");
                    return true;
                }
            }
        }
        backtrack(stk.size() - n);
        decS();
        msg("[backtrack] stmtList -> stmt ;");
        incS();
        if(stmt()){
            if(match(TOKEN_TYPE.SEMI)){
                decS();
                msg("[ok]");
                return true;
            }
        }
        backtrack(stk.size() - n);
        decS();
        msg("[falhou]");
        return false;
    }
    public boolean stmt() throws IOException{
        int n = stk.size();
        msg("stmt -> ID = Exp");
        incS();
        if(match(TOKEN_TYPE.ID)){
            if(match(TOKEN_TYPE.EQ)){
                if(exp()){
                    decS();
                    msg("[ok]");
                    return true;
                }
            }
        }
        backtrack(stk.size() - n);
        decS();
        msg("[backtracking]: stmt -> Exp");
        incS();
        if(exp()){
            decS();
            msg("[ok]");
            return true;
        }
        backtrack(stk.size() - n);
        decS();
        msg("[falhou]");
        return false;
    }
    
    
    public boolean exp() throws IOException{
        int n = stk.size();
        msg("exp -> (NUM|ID) exp1");
        incS();
        if(match(TOKEN_TYPE.ID) || match(TOKEN_TYPE.NUM)){
            if(exp1()){
                decS();
                msg("[ok]");
                return true;
            }
        }
        backtrack(stk.size() - n);
        decS();
        msg("[falhou]");
        return false;    
    }
    
    public boolean exp1() throws IOException{
        msg("exp1 ->  (+|*|-|/) exp exp1");
        incS();
        int n = stk.size();
        if(match(TOKEN_TYPE.PLUS) || match(TOKEN_TYPE.TIMES) || match(TOKEN_TYPE.SUB) || match(TOKEN_TYPE.DIV)){
            if(exp()){
                if(exp1()){
                    decS();
                    msg("[ok]");
                    return true;
                }
            }
        }
        backtrack(stk.size() - n);
        decS();
        msg("[ok]");
        return true;
    }
}
