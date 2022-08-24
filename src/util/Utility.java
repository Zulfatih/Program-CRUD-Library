package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {
    protected static long ambilEntryPerTahun(String penulis, String tahun) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while(data != null){
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            penulis = penulis.replaceAll("\\s+","");

            if(penulis.equalsIgnoreCase(dataScanner.next()) && tahun.equalsIgnoreCase(dataScanner.next())){
                entry = dataScanner.nextInt();
            }
            data = bufferInput.readLine();
        }
        return entry;
    }

    protected static String ambilTahun() throws IOException{
        boolean tahunValid = false;
        Scanner terminalInput = new Scanner(System.in);
        String tahunInput = terminalInput.nextLine();

        while(!tahunValid){
            try {
                Year.parse(tahunInput);
                tahunValid = true;
            }catch (Exception e){
                System.out.println("Format tahun yang anda masukkan salah\nSilahkan ulangi kembali");
                System.out.print("Masukkan tahun terbit (YYYY): ");
                tahunValid = false;
                tahunInput = terminalInput.nextLine();
            }

        }
        return tahunInput;
    }

    protected static boolean cekKeywordsDiDatabase(String[] keywords, boolean isDisplay) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist = false;
        int nomorData = 0;
        if(isDisplay) {
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("| No |\tTahun |\tPenulis                |\tPenerbit     |\tJudul Buku ");
            System.out.println("-----------------------------------------------------------------------------");
        }
        while(data != null){
            //Cek keywords didalam baris
            isExist = true;
            for (String key:keywords){
                isExist = isExist && data.toLowerCase().contains(key.toLowerCase());
            }

            //Jika cocok tampilkan
            if(isExist){
                if(isDisplay){
                    StringTokenizer stringToken = new StringTokenizer(data, ",");
                    nomorData++;
                    stringToken.nextToken();
                    System.out.printf("|%2d ",nomorData);
                    System.out.printf("|\t%s  ",stringToken.nextToken());
                    System.out.printf("|\t%-20s   ",stringToken.nextToken());
                    System.out.printf("|\t%-10s   ",stringToken.nextToken());
                    System.out.printf("|\t%s",stringToken.nextToken());
                    System.out.print("\n");
                }else {
                    break;
                }
            }
            data = bufferInput.readLine();
        }
        if(isDisplay) {
            System.out.println("-----------------------------------------------------------------------------");
        }
        return isExist;
    }

    public static boolean getYesorNo(String message){
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n"+message+" (y/n)? ");
        String pilihanUser = terminalInput.next();

        while(!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")){
            System.err.println("INPUT SALAH!,pilih antara y/n");
            System.out.print("\n"+message+" (y/n)? ");
            pilihanUser = terminalInput.next();
        }

        return pilihanUser.equalsIgnoreCase("y");
    }

    public static void clearScreen() throws IOException {
        try{
            if (System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }else{
                System.out.print("\033\143");
            }
        }catch (Exception ex){
            System.err.println("Tidak bisa clear Screen");
        }
    }
}
