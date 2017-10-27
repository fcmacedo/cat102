package cat102;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ConverteCat102
{
  
  private static final String CNPJ_CDT = "07861033000175";
	
  public static void main(String[] args)
  {
    
	  
	int contador20 = 0;
    int contador30 = 0;
    int contador40 = 0;
    int contador50 = 0;
    int contador60 = 0;
    
    FileReader fr = null;
    FileReader fi = null;
    FileReader fd = null;
    
    List <String> campos = new ArrayList<String>();
    List <String> itens  = new ArrayList<String>();
    List <String> dados  = new ArrayList<String>();
    
    
   // String[] campos = new String[100];
   // String[] itens = new String[100];
   // String[] dados = new String[100];
    int i = 0;
    String destinatario = null;
    String linha = null;
    String linhadados = null;
    String linhaitens = null;
    //String cfopaux = null;
    //String valoraux = null;
    String arquivom = null;
    String arquivoi = null;
    String arquivod = null;
    String conteudo = "\r\n";
    
    File diretorio = new File(".");
    String[] arquivos = diretorio.list();
    
    //Como era antes da alteração de 12/10/2017 (CAT 122): SP0011701NM.001
    //Como ficou                                         : SP07861033000175210011701N01M.001
    
    for (int i1 = 0; i1 < arquivos.length; i1++)
    {
      if ((arquivos[i1].length() >= 5) && ("M".equals(arquivos[i1].substring(arquivos[i1].length() - 5, arquivos[i1].length() - 4)))) {
        arquivom = arquivos[i1];
      }
      if ((arquivos[i1].length() >= 5) && ("I".equals(arquivos[i1].substring(arquivos[i1].length() - 5, arquivos[i1].length() - 4)))) {
        arquivoi = arquivos[i1];
      }
      if ((arquivos[i1].length() >= 5) && ("D".equals(arquivos[i1].substring(arquivos[i1].length() - 5, arquivos[i1].length() - 4)))) {
        arquivod = arquivos[i1];
      }
    }
    
    
    
    String[] mes = { "", "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ" };
    
    FileWriter fileWriter = null;//                                                SP0011701NM.001 SP07861033000175210011701N01M.001
    try
    {
      fileWriter = new FileWriter("CAT102" + mes[Integer.parseInt(arquivom.substring(23, 25))] + arquivom.substring(21, 23) + ".txt");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    PrintWriter writer = new PrintWriter(fileWriter);
    try
    {
      fr = new FileReader(arquivom);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    try
    {
      fi = new FileReader(arquivoi);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    try
    {
      fd = new FileReader(arquivod);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    BufferedReader br = new BufferedReader(fr);
    BufferedReader bi = new BufferedReader(fi);
    BufferedReader bd = new BufferedReader(fd);
    
    int menordata = 99999999;
    int maiordata = 0;
    
    //ok CAT 122
    try
    {
      
    	//Carrega o mestre
    	while ((linha = br.readLine()) != null)
      {
        campos.add(linha);
        if (Integer.parseInt(campos.get(i).substring(81, 89)) < menordata) {
          menordata = Integer.parseInt(campos.get(i).substring(81, 89));
        }
        if (Integer.parseInt(campos.get(i).substring(81, 89)) > maiordata) {
          maiordata = Integer.parseInt(campos.get(i).substring(81, 89));
        }
        i++;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    try
    {
      //Carrega o item
    	while ((linha = bi.readLine()) != null)
      {
        itens.add(linha);
       
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    try
    {
      //carrega o dados
    	while ((linha = bd.readLine()) != null)
      {
        dados.add(linha);
        
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
   
    //REGISTRO 10
    try
    {
      writer.print("10|");
      writer.print("1,00|");
      writer.print(CNPJ_CDT + "|");
      writer.print(Integer.toString(menordata).substring(6, 8) + "/" + Integer.toString(menordata).substring(4, 6) + "/" + Integer.toString(menordata).substring(0, 4) + "|");
      writer.print(Integer.toString(maiordata).substring(6, 8) + "/" + Integer.toString(maiordata).substring(4, 6) + "/" + Integer.toString(maiordata).substring(0, 4));
      writer.print(conteudo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    linha = null;
    linhadados = null;
    for (int j = 0; j < campos.size(); j++)
    {
      linha = campos.get(j);
      if (campos.get(j) == null) {
        break;
      }
      
      //CNPJ ou CPF do Destinatário
      destinatario = null;
      for (int j2 = 0; j2 < dados.size(); j2++)
      {
        linhadados = dados.get(j2);
        if (linhadados.substring(0, 14).equals(linha.substring(0, 14)))
        {
          destinatario = linhadados;
          break;
        }
      }
      
      //Registro 20
      writer.print("20|");
      contador20++;
      
      //campo situacao - Mestre
      if (linha.substring(195, 196) == "S") {
        writer.print("C|");
      } else if (linha.substring(195, 196) == "R") {
        writer.print("R|");
      } else {
        writer.print("I|");
      }
      if (linha.substring(195, 196) == "C") {
        writer.print("CANCELAMENTO DA NF|");
      } else {
        writer.print("|");
      }
      
      
      writer.print("PROVEDORIA|");
      
      //campo serie - Mestre
      if (linha.substring(91, 94) == "U") {
        writer.print("0|");
      } else {
        writer.print(linha.substring(91, 94) + "|");
      }
      
      //campo Numero NF - Mestre
      writer.print(linha.substring(94, 103) + "|");
      
      //Campo Data de Emissao
      writer.print(linha.substring(87, 89) + "/" + linha.substring(85, 87) + "/" + linha.substring(81, 85) + " 00:00:00" + "|");
      
    //Campo Data de Emissao
      writer.print(linha.substring(87, 89) + "/" + linha.substring(85, 87) + "/" + linha.substring(81, 85) + " 00:00:00" + "|");
      
      writer.print("1|");
      
      linhaitens = null;
      //cfopaux = "0";
      //valoraux = "0";
      
      for (int j2 = 0; j2 < itens.size(); j2++)
      {
        if (itens.get(j2) == null) {
          break;
        }
        
        linhaitens = itens.get(j2);
        if ((linhaitens.substring(0, 14).equals(linha.substring(0, 14))) && 
          (linhaitens.substring(33, 42).equals(linha.substring(94, 103))))
        {
            //CFOP
        	writer.print(linhaitens.substring(42, 46) + "|");
          break;
        }
      }
      writer.print("|");
      
      writer.print("|");
      
      //Verifica se é CPF ou CNPJ
      if (destinatario.substring(0, 3).equals("000"))
      {
        //Um CPF???
    	if (destinatario.substring(0, 14).equals("00027096000262")) {
          writer.print(destinatario.substring(0, 14).trim() + "|");
        } else {
          writer.print(destinatario.substring(3, 14).trim() + "|");
        }
      }
      //CNPJ
      else {
        writer.print(destinatario.substring(0, 14).trim() + "|");
      }
      
      //Razão Social
      writer.print(destinatario.substring(28, 63).trim() + "|");
      
      //Endereco
      writer.print(destinatario.substring(63, 108).trim() + "|");
      
      writer.print(destinatario.substring(108, 113) + "|");
      
      writer.print("|");
      
      writer.print(destinatario.substring(136, 151).trim() + "|");
      
      writer.print(destinatario.substring(151, 181).trim() + "|");
      
      writer.print(destinatario.substring(181, 183) + "|");
      
      writer.print("|");
      
      writer.print("|");
      
      writer.print("|");
      
      writer.print(destinatario.substring(14, 28));
      writer.print(conteudo);
      if (!linha.substring(195, 196).equals("C"))
      {
        linhaitens = null;
        for (int j2 = 0; j2 < itens.size(); j2++)
        {
          if (itens.get(j2) == null) {
            break;
          }
          linhaitens = itens.get(j2);
          if ((linhadados.substring(0, 14).equals(linha.substring(0, 14))) && 
            (linhaitens.substring(33, 42).equals(linha.substring(94, 103))))
          {
            writer.print("30|");
            contador30++;
            
            writer.print(linhaitens.substring(49, 59).trim() + "|");
            
            writer.print(linhaitens.substring(59, 99).trim() + "|");
            
            writer.print("|");
            if (linhaitens.substring(103, 109).equals("      ")) {
              writer.print("0|");
            } else {
              writer.print(linhaitens.substring(103, 109) + "|");
            }
            if (Integer.parseInt(linhaitens.substring(109, 116)) == 0) {
              writer.print("1,0000|");
            } else {
              writer.print(linhaitens.substring(109, 116) + "," + linhaitens.substring(116, 120) + "|");
            }
            writer.print(linhaitens.substring(131, 140) + "," + linhaitens.substring(140, 142) + "00" + "|");
            
            writer.print(linhaitens.substring(131, 140) + "," + linhaitens.substring(140, 142) + "|");
            
            writer.print("020|");
            
            writer.print(linhaitens.substring(208, 210) + "," + linhaitens.substring(210, 212) + "|");
            
            writer.print("0,00|");
            
            writer.print("0,00");
            writer.print(conteudo);
          }
        }
        writer.print("40|");
        contador40++;
        
        writer.print(linha.substring(147, 157) + "," + linha.substring(157, 159) + "|");
        writer.flush();
        
        writer.print(linha.substring(159, 169) + "," + linha.substring(169, 171) + "|");
        writer.flush();
        
        writer.print("0,00|");
        writer.flush();
        
        writer.print("0,00|");
        
        writer.print(linha.substring(135, 145) + "," + linha.substring(145, 147) + "|");
        
        writer.print("0,00|");
        
        writer.print("0,00|");
        
        writer.print("0,00|");
        
        writer.print("0,00|");
        
        writer.print("0,00|");
        
        writer.print(linha.substring(135, 145) + "," + linha.substring(145, 147) + "|");
        
        writer.print("0,00|");
        
        writer.print("0,00|");
        
        writer.print("0,00");
        writer.print(conteudo);
        
        writer.print("50|");
        contador50++;
        
        writer.print("0|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("|");
        
        writer.print("0,000|");
        
        writer.print("0,000");
        writer.print(conteudo);
        writer.flush();
        
        writer.print("60|");
        contador60++;
        
        writer.print("0000000|");
        
        writer.print("0000000|");
        
        writer.print("0000000");
        writer.print(conteudo);
        writer.flush();
      }
      writer.flush();
    }
    writer.print("90|");
    
    writer.print(String.format("%05d", new Object[] { Integer.valueOf(contador20) }) + "|");
    
    writer.print(String.format("%05d", new Object[] { Integer.valueOf(contador30) }) + "|");
    
    writer.print(String.format("%05d", new Object[] { Integer.valueOf(contador40) }) + "|");
    
    writer.print(String.format("%05d", new Object[] { Integer.valueOf(contador50) }) + "|");
    
    writer.print(String.format("%05d", new Object[] { Integer.valueOf(contador60) }));
    writer.print(conteudo);
    try
    {
      fr.close();
      br.close();
      writer.flush();
      writer.close();
      fileWriter.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
