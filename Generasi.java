/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alvin
 */
import java.util.*;
//Kelas Generasi untuk menjadi kolam populasi (population pool)
public class Generasi {
    Population populasi = new Population(); //Membuat atribut populasi dan menginisialisaikannya
    Population populasiBerikut; //Membuat atribut untuk menyimpan populasi generasi berikutnya
    int ukuranPopulasi; //Membuat atribut untuk ukuran populasi
    int totalAngkaTabel; //Membuat atribut untuk menghitung berapa banyak angka pada tabel awal
    int panjangTabel; //Membuat atribut untuk menyimpan panjang tabel
    int elitism; //Membuat atribut untuk menyimpan banyakanya individu yang mau dilakukan elitism
    Random rand; //Membuat variabel untuk seedRandom
    
    
    //Method constructor dan menginisialisasi populasi pertama
    public Generasi(int ukuranPopulasi, int totalAngkaTabel, int panjangTabel, int[] tabel, Random rand, int elitism){
        this.ukuranPopulasi = ukuranPopulasi; //Menginisialisasi atribut ukuranPopulasi
        this.totalAngkaTabel = totalAngkaTabel; //Menginisialisasi atribut totalAngkaTabel
        this.panjangTabel = panjangTabel; //Menginisialisasi atribut panjangTabel  
        this.elitism = elitism;//Menginisialisasi atribut elitism
        this.populasi.inisialisasiPopulasi(this.ukuranPopulasi, this.panjangTabel, tabel, this.totalAngkaTabel, rand);//menginisialisasi populasi pertama
        this.rand =rand;//Menginisialisasi atribut untuk seedRandom
    }
    
    
    //Method untuk menseleksi parent dan disimpan dalam atribut populasiBerikut
    /*ide untuk melakukan seleksi parent adalah dengan menggunakan method pada kelas populasi dan membuat populasi yang baru,
    di mana populasi yang baru tersebut diisi dengan parent yang sudah dipilih oleh method memilih parent tersebut*/
    public void seleksiParent(int pilihanSelection) {
        populasiBerikut = new Population(); //menginisialisasi populasi berikut dengan populasi kosong
        //Memasukan populasi berikut dengan array individu yang menjadi parent pada populasi sebelumnya dan memasukan tabel dan banyak angka pada tabel
        populasiBerikut.inisialisasiPopulasiBaru(populasi.getarrParentPopulation(pilihanSelection, elitism));
    }
    
    
    //Method untuk Crossover
    /*Ide untuk melakukan crossover adalah dengan menggunakan  2-point crossover di mana dipilih terlebih dahulu 2 titik secara random sepanjang
    array individu(kromosom) dan titik tidak boleh berada di ujung-ujung kromosom dan tidak boleh sama satu sama lain, lalu tukar bagian yang berada di antara kedua titik tersebut*/
    public void crossover(int pilihanCrossover) {
        //Looping untuk menukar isi parent, diambil 2-2 dari banyaknya populasi
        for(int i =0; i < ukuranPopulasi; i+=2){
            if(pilihanCrossover == 1){
                int titikCrossoverPertama = Math.abs(rand.nextInt(panjangTabel)); //Menentukan titik random pertama untuk crossover pada kromosom
                for (int j = 0; j <panjangTabel; j++) {
                    //crossover dilakukan dengan menukar bagian awal sebelum titik crossover pertama
                    if(j <=titikCrossoverPertama ){//jika titik j berada sebelum titik crossover pertama
                        //jika titik j berada di antara kedua titik crossover
                        int temp = populasiBerikut.arrIndividual[i].arrGene[j];//maka simpan isi dari parent pertama
                        populasiBerikut.arrIndividual[i].arrGene[j] = populasiBerikut.arrIndividual[i+1].arrGene[j];//masukan isi parent kedua ke gene parent pertama
                        populasiBerikut.arrIndividual[i+1].arrGene[j]= temp;//lalu isi parent kedua dengan isi parent pertama
                    }
                }
            }else{//Jika pilihan crossover yang dilakukan adalah dengan menggunakan 2 point crossover
                int titikCrossoverPertama = Math.abs(rand.nextInt(panjangTabel)); //Menentukan titik random pertama untuk crossover pada kromosom
                int titikCrossoverKedua = Math.abs(rand.nextInt(panjangTabel));//Menentukan titik random kedua untuk crossover pada kromosom
                //jika kedua titik adalah sama (memiliki selisih 0) atau akan menukar seluruh kromosom (terjadi ketika salah satu titik berada di 0 dan titik lain berada pada ujung panjang tabel) maka pilih ulang titik random pertama dan kedua
                while(Math.abs(titikCrossoverPertama - titikCrossoverKedua) == 0 || Math.abs(titikCrossoverPertama - titikCrossoverKedua) == (panjangTabel-1)){
                    titikCrossoverPertama = Math.abs(rand.nextInt(panjangTabel)); //Memilih titik random pertama lagi
                    titikCrossoverKedua = Math.abs(rand.nextInt(panjangTabel));//Memilih titik random kedua lagi
                }//Diulang sampai titik tidak sama dan tidak ada pada ujung-ujung kromosom
                for (int j = 0; j <panjangTabel; j++) {
                    //crossover dilakukan dengan menukar bagian tengah dari kedua titik unntuk 2 buah kromosom
                    if((j <=titikCrossoverPertama && j >= titikCrossoverKedua)|| (j <= titikCrossoverKedua && j >= titikCrossoverPertama )){//jika titik j berada di tengah-tengah 2 titik crossover
                        //jika titik j berada di antara kedua titik crossover
                        int temp = populasiBerikut.arrIndividual[i].arrGene[j];//maka simpan isi dari parent pertama
                        populasiBerikut.arrIndividual[i].arrGene[j] = populasiBerikut.arrIndividual[i+1].arrGene[j];//masukan isi parent kedua ke gene parent pertama
                        populasiBerikut.arrIndividual[i+1].arrGene[j]= temp;//lalu isi parent kedua dengan isi parent pertama
                    }
                }
            }
        }
    }
    
    
    //Method untuk melakukan mutasi
    public void mutation(int idx) {
        int mutationPoint = rand.nextInt(panjangTabel); //Memilih titik mutasi secara random
        //menukar isi populasi pada titik mutasi tersebut
        if(populasiBerikut.arrIndividual[idx].arrGene[mutationPoint] == 0){//jika isi 0 pada titik mutasi
            populasiBerikut.arrIndividual[idx].arrGene[mutationPoint] = 1;//maka ganti menjadi 1
        }else{//jika tidak
            populasiBerikut.arrIndividual[idx].arrGene[mutationPoint] = 0;//maka ganti menjadi 0
        }
    }
    
    //Menggantikan populasi yang lama menjadi populasi yang baru.
    public void generasiBerikut() {
        populasi = new Population(); //menginisialisasi populasi kosong untuk variabel populasi
        populasi.inisialisasiPopulasiBaru(populasiBerikut.arrIndividual);//memasukan isi populasi yang disimpan untuk generasi berikutnya kepada populasi sekarang
    }
}
