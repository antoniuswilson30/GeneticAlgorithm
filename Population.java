/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alvin
 */
import java.util.Random;
import java.util.Arrays;
import java.util.Comparator;
//Kelas untuk menjadi populasi kromosom
class Population {
    //Variabel dibuat static karena hany perlu diinisialsasi sekali saja
    static int ukuranPopulasi;//Variabel ukuranPopulasi untuk menyimpan banyaknya kromosom untuk setiap populasi
    static int[] tabel;//Variable tabel untuk menyimpan tabel soal yang dimasukan
    static int totalAngkaTabel;//Variable totalAngkaTabel untuk menghitung banyaknya angka yang berada pada tabel soal
    static Random rand;
    
    Individual[] arrIndividual;//Variabel arrIndividual untuk merepresentasikan populasi 
    int fittest;//Variabel fittest untuk menyimpan fitness terbaik dari sebuah populasi
    int leastFittest;//Variabel leastFittest untuk menyimpan fitness terburuk dari sebuah populasi
    //Metod untuk menginisialisasi populasi baru menggunakan hanya array of Individual
    public void inisialisasiPopulasiBaru(Individual[] arrIndividual){
        this.arrIndividual = new Individual[ukuranPopulasi];
        System.arraycopy(arrIndividual, 0, this.arrIndividual, 0, ukuranPopulasi);
    }

    //Inisialisasi Populasi dari variable ukuran
    public void inisialisasiPopulasi(int size, int geneLength, int[] tabel, int totalAngkaTabel, Random rand) {
        this.totalAngkaTabel = totalAngkaTabel;
        this.tabel = tabel;
        this.ukuranPopulasi = size;
        this.arrIndividual = new Individual[this.ukuranPopulasi];
        this.rand = rand;
        for (int i = 0; i < arrIndividual.length; i++) {
            arrIndividual[i] = new Individual(geneLength, tabel, totalAngkaTabel, rand);
        }
    }

    //Metode untuk mendapatkan individu terbaik
    public Individual individuTerbaik() {
        int maxfittest = Integer.MIN_VALUE; //set maximum fittest dengan negatif infinit
        int idx_maxfittest = 0; //index awal 0
        for (int i = 0; i < arrIndividual.length; i++) { //loop sepanjang array untuk mencari fittest tertinggi
            if (maxfittest <= arrIndividual[i].fitness) { //cek fittest individu ke-i lebih besar dari maxfittest saat ini
                maxfittest = arrIndividual[i].fitness; //jika lebih besar, update maxfittest
                idx_maxfittest = i; //update index individu yang punya maxfittest
            }
        }
        fittest = arrIndividual[idx_maxfittest].fitness; //update fittest
        return arrIndividual[idx_maxfittest]; //kembalikan individu dengan fittest tertinggi
    }
    
    //Metode untuk mendapatkan individu terburuk
    public Individual individuTerburuk() {
        int minFittest = Integer.MAX_VALUE; //set minimum fittest dengan positif infinit
        int idx_minFittest = 0; //index awal 0
        for (int i = 0; i < arrIndividual.length; i++) { //loop sepanjang array untuk mencari fittest terendah
            if (minFittest >= arrIndividual[i].fitness) { //jika fittest individu ke-1 lebih kecil dari minFittest saat ini
                minFittest = arrIndividual[i].fitness; //update minfittest
                idx_minFittest = i; //update index individu yang punya minfittest
            }
        }
        leastFittest = arrIndividual[idx_minFittest].fitness; //update leastfittest
        return arrIndividual[idx_minFittest]; //kembalikan individu dengan fittest terendah
    }
    
    public Individual[] getarrParentPopulation(int pilihanSelection, int elitism){
        Individual[] arrParent = new Individual[this.ukuranPopulasi]; //inisiasi array of individu untuk menyimpan parent
        elitism(arrParent, elitism);//Melakukan elitism pada arrayParent;
        for(int i =elitism; i < this.ukuranPopulasi; i++){ //loop sebanyak ukuran populasi
            if(pilihanSelection == 1){
                arrParent[i] = new Individual(this.rouletteWheelSelecetion()); //setiap parent merupakan parent baru yang didapatkan dari selection menggunakan roulette wheel selection
            }else{
                arrParent[i] = new Individual(this.rankSelection()); //setiap parent merupakan parent baru yang didapatkan dari selection menggunakan rank selection
            }
        }
        return arrParent; 
    }
    
    //method roulette wheel untuk melakukan selection mencari parent dari populasi
    /*ide melakukan pemilihan roulette wheel adalah dengan menghitung jumlah keseluruhan dari fitness yang ada, lalu memilih angka random antari 1 sampai jumlah sum tersebut,
    lalu menambahkan fitness yang ada pada variabel lain dan jika variabel tersebut sudah melebihi angka random tersebut maka individu yang memiliki fitness yang membuat jumlah
    sementara berhasil melebihi angka random tersebut akan dipilih*/
    public Individual rouletteWheelSelecetion(){
        int totalSum = 0;//Membuat variabel untuk sum total
        for(int i =0; i < this.ukuranPopulasi; i++){
            totalSum += this.arrIndividual[i].fitness;//Menambahkan seluruh fitness kepada totalSum
        }
        int random = rand.nextInt(totalSum-1);//Memilih angka random dari 1 sampai totalSum
        random++;//Menambahkan 1 kepada randomPoint agar kemungkinan random menjadi 1 sampai jumlah sum
        int sum = 0;//Membuat variabel untuk sum sementara
        for(int i =0; i < this.ukuranPopulasi; i++){//Menjalani array dari depan
            sum += this.arrIndividual[i].fitness;//Dan menambahkan nya kepada variabel sum sementara
            if(sum >= random){//Jika variabel sum sementara sudah lebih sama dengan variabel random
                return this.arrIndividual[i];//Maka individual yang terpilih adalah individual yang fitnessnya berhasil membuat variabel sum lebih dari sama dengan random
            }
        }
        return null;
    }
    
    //method rank selection untuk melakukan selection mencari parent dari populasi
    /*ide melakukan pemilihan rank adalah dengan mengurutkan fitness dari populasi dan memilih angka random dari 1 sampai (ukuranPopulasi*(ukuranPopulasi+1)/2), lalu menjumlahkan dari 1 sampai
    ukuran populasi sesuai ranking individu dimana individu terkecil memiliki ranking 1, jika hasil penjumlahan sudah lebih dari angka random tersebut, maka individu yang memiliki ranking yang membuat hasil penjumlahan
    lebih dari pada angka random tersebut adalah individu yang dipilih*/
    public Individual rankSelection(){
        int[][] arrTemp = new int[ukuranPopulasi][2]; //Membuat array 2d untuk menyimpan fitness dalam populasi, dimana index 0 mendandakan fitness nya dan 1 menandakan index nya pada arrayIndividual
        for(int i =0; i < ukuranPopulasi; i++){//Memasukan fitness dan indexdari setiap inidividu ke dalam array 
            arrTemp[i][0] = this.arrIndividual[i].fitness;
            arrTemp[i][1] = i;
        }
        Arrays.sort(arrTemp, (int[] pertama, int[] kedua) -> {
            if(pertama[0] < kedua[0]){
                return -1;
            }else if(pertama[0] > kedua[0]){
                return 1;
            }else{
                return 0;
            }
        });//Mengurutkan fitness dari array 2d menggunakan lambda dan method arrays.sort dari library kecil ke besar
        int randomPoint;//Membuat variabel untuk menyimpan titik random
        if(ukuranPopulasi%2 == 0){
            randomPoint = rand.nextInt((ukuranPopulasi/2)*(ukuranPopulasi+1));//jika ukuranPopulasi genap maka perhitungan dilakukan dengan ukuranPopulasi dibagi 2 terlebih dahulu untuk meringankan perhitungan
        }else{
            randomPoint = rand.nextInt((ukuranPopulasi)*((ukuranPopulasi+1)/2));//jika tidak, maka perhitungan dilakukan dengan membagi (ukuranPopulasi+1) terlebih dahulu
        }
        randomPoint++;//Menambahkan 1 kepada randomPoint agar kemungkinan random menjadi 1 sampai (ukuranPopulasi*(ukuranPopulasi+1)/2)
        int sum = 0;//Membuat variabel untuk sum sementara
        for(int i =1; i <= this.ukuranPopulasi; i++){//Menjalani dari 1 sampai ukuranPopulasi
            sum += i;//Dan menambahkan nya kepada variabel sum sementara
            if(sum >= randomPoint){//Jika variabel sum sementara sudah lebih sama dengan variabel random
                return this.arrIndividual[arrTemp[i-1][1]];//Maka individual yang terpilih adalah individual yang fitnessnya berhasil membuat variabel sum lebih dari sama dengan random
            }
        }
        return null;
    }
    
    //Method untuk melakukan elitism
    public void elitism(Individual arr[], int elitism){
        int[][] arrTemp = new int[ukuranPopulasi][2]; //Membuat array 2d untuk menyimpan fitness dalam populasi, dimana index 0 mendandakan fitness nya dan 1 menandakan index nya pada arrayIndividual
        for(int i =0; i < ukuranPopulasi; i++){//Memasukan fitness dan indexdari setiap inidividu ke dalam array 
            arrTemp[i][0] = this.arrIndividual[i].fitness;
            arrTemp[i][1] = i;
        }
        Arrays.sort(arrTemp, (int[] pertama, int[] kedua) -> {
            if(pertama[0] > kedua[0]){
                return -1;
            }else if(pertama[0] < kedua[0]){
                return 1;
            }else{
                return 0;
            }
        });//Mengurutkan fitness dari array 2d menggunakan lambda dan method arrays.sort dari library dari besar ke kecil
        for(int i =0; i < elitism; i++){//For loop untuk memasukan individual yang unggul kepada array parent
            arr[i] = this.arrIndividual[arrTemp[i][1]];
        }
    }

    //menghitung fitness setiap individu
    public void hitungFitness() {
        for (Individual individual : arrIndividual) {
            individual.nilaiFitness();
        }
        this.individuTerbaik();
        this.individuTerburuk();
    }
}
