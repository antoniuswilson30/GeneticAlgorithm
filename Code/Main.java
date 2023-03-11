package Code;
/*

/*
referensi:
https://gist.github.com/Vini2/bd22b36ddc69c5327097921f5118b709 untuk kode genetic algorithm

https://www.youtube.com/watch?v=9JzFcGdpT8E untuk roulette wheel selection

https://linuxhint.com/sort-2d-array-in-java/ untuk sorting array 2d
*/
/*
Format file input
(angka long untuk seed random)
(angka integer untuk banyak populasi)
(angka integer untuk ukuran papan)
(angka untuk batas generasi)
(angka integer untuk probabilitas mutasi (1/angkaInput))
(angka 1 atau 2 untuk metode selection (1 = Roulette wheel, 2 = Rank))
(angka 1 atau 2 untuk metod crossover (1= 1-point Crossover, 2 = 2-Point Crossover))
(angka integer untuk banyak individu masuk elitisim)
(angka sebanyak ukuran papan * ukuran papan untuk input tabel (-1 berarti kotak kosong))
*/
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Path nama file input: ");
        String namaFile = sc.next();
        FileInputStream fis = new FileInputStream(namaFile);
        Scanner fileReader = new Scanner(fis);
        long seedRandom = fileReader.nextLong();//Membuat variabel untuk variabel random
        Random rand = new Random(seedRandom);
        int ukuranPopulasi = fileReader.nextInt();//Menginisialisasi variabel untuk ukuran populasi
        int panjangTabel = fileReader.nextInt();//Menginisialisasi variabel untuk panjang tabel
        //Menginisialisi panjangTabel dengan kuadrat dari input, karena untuk memetakan array 2 dimensi ke 1 dimensi maka panjang tabel 1 dimensi merupakan kuadrat dari
        //panjang array 2 dimensi (contoh: array 2 dimensi dengan ukuran 5x5 diubah menjadi array 1 dimensi memerlukan ukuran 25
        panjangTabel = panjangTabel *panjangTabel;
        int batasGenerasi = fileReader.nextInt();//Menginisialisasi variabel untuk batas generasi yang akan dicoba
        int probabilitasMutasi = fileReader.nextInt();//Menginisialisasi variabel untuk probabilitas mutasi (probabilitas akhir akan menjadi 1/probabilitasMutasi)
        int[] tabel = new int[panjangTabel];//Membuat variabel untuk menyimpan soal pada array of int
        int pilihanSelection = fileReader.nextInt();//Membuat variabel untuk memilih selection apa yang akan dilakukan (Roulette wheel = 1, Rank =2)
        int pilihanCrossover = fileReader.nextInt();//Membuat variabel untuk memilih crossover apa yang akan dilakukan (1-point crossover = 1, 2-point crossover =2)
        int banyakElitism = fileReader.nextInt();//Membaut variabel untuk menentukan berapa banyak individu yang akan dipilih dalam proses elitism
        int totalAngkaTabel =0; //variable ini digunakan untuk menghitung kotak yg tidak kosong atau terisi angka
        for(int i =0; i < panjangTabel; i++){ //input tabel mineswipper
            tabel[i] = fileReader.nextInt();
            if(tabel[i] != -1){ //setiap ada input kotak yg berisi angka
                totalAngkaTabel ++; //update jumlahnya
            }
        }
        fileReader.close();
        double Time_start = System.nanoTime();
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter((namaFile.substring(0, namaFile.length()-4)+ "Res.txt")));
        long generationCount = 1; //Membuat atribut untuk menghitung generasi
        Generasi generasi = new Generasi(ukuranPopulasi, totalAngkaTabel, panjangTabel, tabel, rand, banyakElitism); //inisiasi generasi baru
        generasi.populasi.hitungFitness(); //menghitung fittest dari generasi
        do{ //lakukan pencarian generasi baru untuk menemukan hasil selama fittest masih kurang dari
            System.out.println("Tes");
            fileWriter.write("Generasi ke-" + generationCount + " Fitness terbesar: " + generasi.populasi.fittest +" Fitness terkecil: " +generasi.populasi.leastFittest + " Fitness benar: " + generasi.totalAngkaTabel*9*10);
            fileWriter.append("\n");
            if(generasi.populasi.fittest < generasi.totalAngkaTabel*10*9){ //selama belum menemukan hasil yang optimal,
                generationCount++; //update jumlah generasi
                generasi.seleksiParent(pilihanSelection); //lakukan pencarian parent
                generasi.crossover(pilihanCrossover); //kemudian crossover
                //dan kemungkinan terjadinya mutasi dalam membentuk generasi baru adalah 1/10000 untuk setiap anggota dalam populasi
                for(int i = 0; i < generasi.ukuranPopulasi; i++){
                    if (rand.nextInt(probabilitasMutasi) < 1) {//Jika masuk kedalam kemungkinan mutaasi
                        generasi.mutation(i);//Maka lakukan mutasi untuk individu tersebut
                    }
                }
                generasi.generasiBerikut();//Melakukan inisialisasi generasi berikutnya menggunakan method generasi
                generasi.populasi.hitungFitness(); //menghitung fittest dari generasi baru
            }
        }while (generasi.populasi.fittest < generasi.totalAngkaTabel * 10*9 && generationCount < batasGenerasi);//Looping akan diulang sampai solusi sudah ketemu atau generasi sudah mencapai batas
        if(generasi.populasi.fittest == (generasi.totalAngkaTabel*10*9)){//Jika solusi ketemu maka keluarkan output yang sesuai
            fileWriter.write("Generasi solusi berada pada generasi populasi ke-" + generationCount);
        }else{//Jika tidak maka solusi hanya sub-optimal
            fileWriter.write("Pencobaan generasi berhenti pada generasi populasi ke-" + generationCount);
        }
        fileWriter.append("\n");
        double Time_finish = System.nanoTime();
        double waktuTotal = (Time_finish - Time_start) / 1000000000;
         fileWriter.write("Waktu eksekusi: " + waktuTotal + " detik");
        fileWriter.append("\n");
        //Output hasil akhir
        fileWriter.write("Fitness akhir: "+ generasi.populasi.fittest + "/"+generasi.totalAngkaTabel*10*9);
        fileWriter.append("\n");
        fileWriter.write("Genes terbaik: ");
        fileWriter.append("\n");
        int len = (int) Math.sqrt(panjangTabel);//Mengambil akar dari panjang untuk memetakan output pada array 2D
        //Mengeluarkan output
        for (int i = 0; i < len; i++) {
            for(int j =0; j < len; j++){
                fileWriter.write(generasi.populasi.individuTerbaik().arrGene[i*len + j] + " ");
            }
            fileWriter.append("\n");
        }
        fileWriter.close();
    }
}
