package util;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operasi {
    public static void tampilkanData() throws IOException {
        FileReader fileInput;
        BufferedReader bufferInput;

        try{
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        }catch (Exception e){
            System.err.println("Database tidak ditemukan\nSilahkan tambah data terlebih dahulu");
            tambahData();
            return;
        }

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("| No |\tTahun |\tPenulis                |\tPenerbit     |\tJudul Buku");
        System.out.println("-----------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        int nomorData = 0;

        while(data != null){
            StringTokenizer stringToken = new StringTokenizer(data, ",");
            nomorData++;
            stringToken.nextToken();
            System.out.printf("|%2d ",nomorData);
            System.out.printf("|\t%s  ",stringToken.nextToken());
            System.out.printf("|\t%-20s   ",stringToken.nextToken());
            System.out.printf("|\t%-10s   ",stringToken.nextToken());
            System.out.printf("|\t%s",stringToken.nextToken());
            System.out.print("\n");

            data = bufferInput.readLine();
        }
        System.out.println("-----------------------------------------------------------------------------");

    }

    public static void cariData() throws IOException{

        // Membaca database ada atau tidak
        try{
            File file = new File("database.txt");
        }catch (Exception e){
            System.err.println("Database tidak ada\nSilahkan tambah data terlebih dahulu");
            tambahData();
            return;
        }

        // Mengambil keyword dari user
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Apa yang kamu mau cari : ");
        String cariString = terminalInput.nextLine();

        String[] keywords = cariString.split("\\s+");

        // Menyamakan keyword dari user dengan yang ada di database
        Utility.cekKeywordsDiDatabase(keywords,true);

    }

    public static void tambahData() throws IOException{

        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // mengambil input dari user
        Scanner terminalInput = new Scanner(System.in);
        String penulis, judul, penerbit, tahun;

        System.out.print("Masukkan nama penulis  : ");
        penulis = terminalInput.nextLine();
        System.out.print("Masukkan judul buku    : ");
        judul = terminalInput.nextLine();
        System.out.print("Masukkan nama penerbit : ");
        penerbit = terminalInput.nextLine();
        System.out.print("Masukkan tahun terbit  : ");
        tahun = Utility.ambilTahun();

        // cek buku di database
        String[] keywords = {tahun+","+penulis+","+penerbit+","+judul};

        boolean isExist = Utility.cekKeywordsDiDatabase(keywords, false);

        // menulis input ke database
        if(!isExist){
            long nomorEntry = Utility.ambilEntryPerTahun(penulis,tahun) + 1;
            String penulisTanpaSpasi = penulis.replaceAll("\\s+","");
            String primaryKey = penulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;
            System.out.println("\nData yang anda masukkan adalah");
            System.out.println("----------------------------------");
            System.out.println("Primary Key  : " + primaryKey);
            System.out.println("Nama Penulis : " + penulis);
            System.out.println("Judul Buku   : " + judul);
            System.out.println("Tahun terbit : " + tahun);
            System.out.println("Penerbit     : " + penerbit);

            boolean isTambah = Utility.getYesorNo("Tambahkan ke database");

            if (isTambah){
                bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        }else {
            System.out.println("Buku serupa sudah ada di dalam database dengan data berikut : ");
            Utility.cekKeywordsDiDatabase(keywords, true);
        }

        bufferOutput.close();

    }

    public static void updateData() throws IOException{
        // mengambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // membuat temporary database
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        //menampilkan database
        tampilkanData();

        //mengambil user input
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukkan nomor data yang ingin diubah : ");
        int updateNum = terminalInput.nextInt();

        //menampilkan data yang ingin di update
        int entryCounts = 0;
        String data = bufferInput.readLine();

        while(data != null) {
            entryCounts++;
            StringTokenizer stringTokenizer = new StringTokenizer(data, ",");

            if (updateNum == entryCounts) {
                System.out.println("Data yang ingin anda ubah : ");
                System.out.println("-------------------------------");
                System.out.println("Primary Key  : " + stringTokenizer.nextToken());
                System.out.println("Tahun terbit : " + stringTokenizer.nextToken());
                System.out.println("Penulis Buku : " + stringTokenizer.nextToken());
                System.out.println("Penerbit     : " + stringTokenizer.nextToken());
                System.out.println("Judul Buku   : " + stringTokenizer.nextToken());

                //update data

                //mengambil input dari user untuk merubah data
                String[] fieldData = {"Tahun","penulis","Penerbit","Judul"};
                String[] tempData = new String[4];

                //refresh token
                stringTokenizer = new StringTokenizer(data,",");
                String originalData = stringTokenizer.nextToken();

                for (int i=0; i< fieldData.length; i++) {

                    boolean isUpdate = Utility.getYesorNo("apakah anda ingin merubah " + fieldData[i]);
                    originalData = stringTokenizer.nextToken();
                    if (isUpdate){
                        //user input
                        if (fieldData[i].equalsIgnoreCase("tahun")){
                            System.out.print("Masukkan tahun terbit baru(YYYY): ");
                            tempData[i] = Utility.ambilTahun();
                        }else{
                            terminalInput = new Scanner(System.in);
                            System.out.print("Masukkan " + fieldData[i] + " baru : ");
                            tempData[i] = terminalInput.nextLine();
                        }
                    }else {
                        tempData[i] = originalData;
                    }
                }

                //menampilkan update baru ke layar
                stringTokenizer = new StringTokenizer(data,",");
                stringTokenizer.nextToken();
                System.out.println("\nBerikut adalah data baru yang anda input : ");
                System.out.println("---------------------------------------------------");
                System.out.println("Tahun terbit : " + stringTokenizer.nextToken() + " ----> " + tempData[0]);
                System.out.println("Penulis Buku : " + stringTokenizer.nextToken() + " ----> " + tempData[1]);
                System.out.println("Penerbit     : " + stringTokenizer.nextToken() + " ----> " + tempData[2]);
                System.out.println("Judul Buku   : " + stringTokenizer.nextToken() + " ----> " + tempData[3]);

                boolean isUpdate = Utility.getYesorNo("Anda yakin ingin merubah data tersebut");

                if (isUpdate){
                    //cek data buku di database
                    boolean isExist = Utility.cekKeywordsDiDatabase(tempData, false);
                    if (isExist){
                        System.err.println("Data yang anda masukkan sudah ada didalam database kami");
                        bufferOutput.write(data);
                    }else {
                        //format baru kedalam database
                        String tahun = tempData[0];
                        String penulis = tempData[1];
                        String penerbit = tempData[2];
                        String judul = tempData[3];

                        //buat primary key
                        long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;

                        String penulisTanpaSpasi = penulis.replaceAll("\\s+","");
                        String primaryKey = penulisTanpaSpasi + "_" + tahun + "_" + nomorEntry;

                        //tulis data ke database
                        bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                    }

                }else{
                    //copy data saja
                    bufferOutput.write(data);
                }

            }else {
                //copy data
                bufferOutput.write(data);
            }
            data = bufferInput.readLine();
            bufferOutput.newLine();
        }

        //menulis data ke database
        bufferOutput.flush();

        fileInput.close();
        fileOutput.close();
        bufferInput.close();

        System.gc();

        //menghapus database
        database.delete();

        //mereplace tempDB menjadi database
        tempDB.renameTo(database);
    }

    public static void hapusData() throws IOException{
        // ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferInput = new BufferedReader(fileInput);

        // buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferWriter = new BufferedWriter(fileOutput);

        // tampilkan data
        tampilkanData();

        //ambil user iunput untuk menghapus data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukkan nomor buku yang akan dihapus: ");
        int deleteNum = terminalInput.nextInt();

        // looping untuk menghapus tiap data baris dan skip data yang dihapus

        int entryCounts = 0;

        String data = bufferInput.readLine();

        while(data != null){

            entryCounts++;
            boolean isDelete = false;
            boolean isFound = false;
            StringTokenizer stringTokenizer = new StringTokenizer(data,",");

            if (deleteNum == entryCounts){
                System.out.println("Data yang ingin anda hapus : ");
                System.out.println("-------------------------------");
                System.out.println("Primary Key  : " + stringTokenizer.nextToken());
                System.out.println("Tahun terbit : " + stringTokenizer.nextToken());
                System.out.println("Penulis Buku : " + stringTokenizer.nextToken());
                System.out.println("Penerbit     : " + stringTokenizer.nextToken());
                System.out.println("Judul Buku   : " + stringTokenizer.nextToken());

                isDelete = Utility.getYesorNo("Apakah anda yakin ingin menghapus data tersebut");
                isFound = true;
            }

            if (isDelete){
                System.out.println("Data Berhasil dihapus");
            }else {
                bufferWriter.write(data);
                bufferWriter.newLine();
            }
            if (isFound){

            }else {
                System.out.println("Data tidak ditemukan");
            }

            data = bufferInput.readLine();
        }


        // menulis data ke file
        bufferWriter.flush();

        fileInput.close();
        fileOutput.close();
        bufferInput.close();
        bufferWriter.close();

        System.gc();

        // menghapus data original
        database.delete();
        // rename file sementara menjadi database
        tempDB.renameTo(database);
    }
}
