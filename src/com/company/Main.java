package com.company;

import util.Operasi;
import util.Utility;

import java.io.*;
import java.nio.file.FileSystemException;
import java.time.Year;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner terminalInput = new Scanner(System.in);
        String pilihanUser;
        boolean isLanjutkan = true;

        while (isLanjutkan) {
            Utility.clearScreen();
            System.out.println("-Database Perpustakaan-\n");

            System.out.println("1.\tLihat seluruh data buku");
            System.out.println("2.\tCari data buku");
            System.out.println("3.\tTambah data buku");
            System.out.println("4.\tUbah data buku");
            System.out.println("5.\tHapus data buku");

            System.out.print("\nPilihan Anda: ");
            pilihanUser = terminalInput.next();

            switch (pilihanUser) {
                case "1":
                    System.out.println("\n===================");
                    System.out.println("-SELURUH DATA BUKU-");
                    System.out.println("===================");
                    Operasi.tampilkanData();
                    break;
                case "2":
                    System.out.println("\n================");
                    System.out.println("-CARI DATA BUKU-");
                    System.out.println("================");
                    Operasi.cariData();
                    break;
                case "3":
                    System.out.println("\n==================");
                    System.out.println("-TAMBAH DATA BUKU-");
                    System.out.println("==================");
                    Operasi.tambahData();
                    Operasi.tampilkanData();
                    break;
                case "4":
                    System.out.println("\n================");
                    System.out.println("-UBAH DATA BUKU-");
                    System.out.println("================");
                    Operasi.updateData();

                    break;
                case "5":
                    System.out.println("\n=================");
                    System.out.println("-HAPUS DATA BUKU-");
                    System.out.println("=================");
                    Operasi.hapusData();
                    break;
                default:
                    System.err.println("Input anda tidak ditemukan\nSilahkan masukan nilai [1-5]");
            }
            isLanjutkan = Utility.getYesorNo("\nAnda ingin melanjutkan");
        }
    }

}
