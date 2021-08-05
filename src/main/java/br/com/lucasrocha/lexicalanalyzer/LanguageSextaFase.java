package br.com.lucasrocha.lexicalanalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.FileReader;

public class LanguageSextaFase {
    public static void main(String[] args) throws IOException {
        int cont = 1;
        try{
            FileReader arq = new FileReader("C:\\Users\\Lucas\\Desktop\\TP1\\Analisador lexico\\tp1\\mavenproject1\\src\\main\\java\\br\\com\\lucasrocha\\syntaticanalyzer\\exemplo1.txt");
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
        } catch (RuntimeException f) {
            System.out.println("Erro léxico aproximadamente na linha: " + cont);
        }
        
        
       

    }
}
