package br.com.lucasrocha.syntaticanalyzer;

import br.com.lucasrocha.lexicalanalyzer.LexicalAnalyzer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class Teste2 {
    public static void main(String args[]) throws IOException{
       int cont = 1;
       try{
            // Primeiro realiza a análise de erros léxicos no arquivo exemplo1.txt
     
            try{
                FileReader arq = new FileReader("C:\\Users\\Lucas\\Desktop\\TP2\\mavenproject1\\src\\main\\java\\br\\com\\lucasrocha\\syntaticanalyzer\\exemplo1.txt");
                BufferedReader lerArq = new BufferedReader(arq);
                String linha = lerArq.readLine();

                while (linha != null) {

                    LexicalAnalyzer lexical = new LexicalAnalyzer(new StringReader(linha));
                    lexical.yylex();
                    System.out.println();
                    cont++;

                linha = lerArq.readLine(); // lê da segunda até a última linha
              }

              arq.close();

            } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
              e.getMessage());
            }

            //Segunda etapa e analisar os erros sintaticos

            langParser2 p = new langParser2("C:\\Users\\Lucas\\Desktop\\TP2\\mavenproject1\\src\\main\\java\\br\\com\\lucasrocha\\syntaticanalyzer\\exemplo1.txt");
             if(p.prog()){
                 System.out.println("Aceito");
             }else{
                 System.out.println("Rejeitado");
             }
             
       } catch (RuntimeException f) {
            System.out.println("Erro léxico aproximadamente na linha: " + cont);
        }
    }
}
