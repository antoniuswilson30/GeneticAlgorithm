package Code;
import java.util.Random;
//Kelas untuk menjadi setiap kromosom
class Individual {
    //Variabel dibuat static karena hany perlu diinisialsasi sekali saja
    static int[] tabel;//Variable tabel untuk menyimpan tabel soal yang dimasukan
    static int totalAngkaTabel;//Variable totalAngkaTabel untuk menghitung banyaknya angka yang berada pada tabel soal
    static int panjangTabel;//Variabel panjangTabel untuk menyimpan panjang tabel
    static Random rand;//Variabel rand untuk menggunakan random yang sudah di inisialisasi di awal
    
    int[] arrGene;//Variabel array of int untuk merepresentasikan kromosom
    int fitness;//Variabel fitness untuk menyimpan fitness dari kromosom tersebut
    
    
    //Constructor kelas individual dibuat menjadi 2, pertama untuk mengcopy kelas Individual dari Individual yang lain
    public Individual(Individual curr){
        this.arrGene = new int[curr.arrGene.length];//Membuat array gene yang baru
        System.arraycopy(curr.arrGene, 0, this.arrGene, 0, curr.arrGene.length);//Mengcopy isi array pada individual yang lain tersebut ke dalam  individual sekarang
    }
    
    //Yang kedua adalah untuk menginisialisasi kelas individual baru dari variabel-variabel pada parameter
    public Individual(int panjangTabel, int[] tabel, int totalAngkaTabel, Random rand) {
        this.rand = rand; 
        this.totalAngkaTabel = totalAngkaTabel; //Menginisialisasi totalAnkaTabel dari input
        this.panjangTabel = panjangTabel;//Menginisialisasi panjangTabel dari input
        this.arrGene = new int[panjangTabel];//Membuat array baru dengan ukuran panjangTabel
        for (int i = 0; i < this.panjangTabel; i++) {//Looping sepanjang panjangTabel
            arrGene[i] = rand.nextInt(2);//Keluarkan isi random antara 0 dan 1, dimana 0 berarti kotak putih dan 1 adalah kotak hitam
        }
        this.tabel = new int[tabel.length];//Membuat array baru untuk tabel soal
        System.arraycopy(tabel, 0, this.tabel, 0, tabel.length);//Mengcopy isi tabel dari input
    }

    //Metode untuk menghitung nilai fitness
    /*Ide untuk menghitung nilai fitness adalah pertama dengan menentukan fitness jika solusi yang berada pada kromosom sudah benar, yaitu banyaknya angka tabel
    yang ada pada kromosom di kali 9 karena 1 angka bisa mempengaruhi 9 kotak, lalu dikali 10 karena angka bisa terdiri dari 0-9. Lalu counter menelusuri tabel dan jika
    tabel bukan merupakan -1 (kosong) maka cari dari kotak-kotak sekelilingnya, jika terdapat perbedaan dengan angka aslinya (misalnya angka adalah 6 namun hanya ada 3 kotak hitam)
    maka fitness yang benar tersebut dikurang sebanyak selisih angka dikali dengan angka yang terdapat pada tabel+ 1 (agar menghindari perkalian dengan 0). Hal ini dilakukan 
    untuk bisa membedakan jika angka pada tabel yang salah adalah angka yang besar atau yang kecil*/
    public void nilaiFitness() {
        fitness = this.totalAngkaTabel*10*9;//Menginisialisasi angka fitness awal
        int len = (int)Math.sqrt(tabel.length);//Membuat variabel panjang tabel 2 dimensi yang awal, untuk memudahkan pemetaan kembali array 1 dimensi menjadi 2 dimensi
        for (int i = 0; i < len; i++){//Untuk setiap baris dari tabel
            for(int j =0; j < len;j++){//Dan untuk setiap kolom dari tabel
                //jika isi tabel tersebut bukan -1 (kosong) maka perhitungan akan dilakukan
                if(tabel[((i*len) + j)] != -1){//Jika isi tabel bukan -1, (pemetaan tabel 2 dimensi menjadi array 1 dimensi adalah dengan mengkalikan panjang array 2 dimensi kepada variabel baris (jika pada tabel titik adalah baris 2 dan kolom 3, maka pada array 1 dimensi berada pada kotak 13
                    int counter = 0;//Membuat variable counter untuk menghitung banyaknya angka 1 (kotak hitam)
                    //Metode perhitungan counter adalah dengan memeriksa apakah i berada di salah satu ujung atau tidak lalu memeriksai apakah j berada di salah satu ujung atau tidak. Lalu mencari kotak-kotak sekitarnya dan mengupdate counter sesuai dengan apakah ada angka 1 atau tidak
                    if(i != 0 && i != (len-1)){//Memeriksa apakah i berada di salah satu ujung tabel
                        if(j != 0 && j != (len-1)){//Memeriksa apakah j berada di salah satu ujung tabel
                            //Jika tidak maka terdapat 9 kotak yang diperiksa menggunakan for loop, dari (i-1, j-1) sampai (i+1, j+1)
                            for(int k = i-1; k <= i +1; k++){
                                for(int p = j-1; p <= j +1; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }else if(j == 0){//Jika j berada pada ujung 0
                            //Maka terdapat 6 kotak yang diperiksa menggunakan for loop, dari (i-1, j) sampai (i+1, j+1)
                            for(int k = i-1; k <= i +1 ; k++){
                                for(int p = j; p <= j+1; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }else if(j == (len-1)){//Jika J berada pada ujung tabel bukan 0
                            //Maka terdapat 6 kotak yang diperiksa menggunakan for loop, dari (i-1, j-1) sampai (i+1, j)
                            for(int k = i-1; k <= i +1 ; k++){
                                for(int p = j-1; p <= j; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }
                    }else if(i == 0){//Jika i berada pada ujung 0
                        if(j != 0 && j != (len-1)){//Dan j bukan berada pada salah satu ujung tabel
                            //Maka terdapat 6 kotak yang diperiksa menggunakan for loop, dari (i, j-1) sampai (i+1, j+1)
                            for(int k = i; k <= i +1; k++){
                                for(int p = j-1; p <= j +1; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }else if(j == 0){//Jika j berada pada ujung 0
                            //Maka terdapat 4 kotak yang diperiksa menggunakan for loop, dari (i, j) sampai (i+1, j+1)
                            for(int k = i; k <= i +1 ; k++){
                                for(int p = j; p <= j+1; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }else if(j == (len-1)){//Jika j berada pada ujung tabel bukan 0
                            //Maka terdapat 4 kotak yang diperiksa menggunakan for loop, dari (i, j-1) sampai (i+1, j)
                            for(int k = i; k <= i +1 ; k++){
                                for(int p = j-1; p <= j; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }
                    }else if(i == (len-1)){//Jika i berada pada ujung tabel bukan 0
                        if(j != 0 && j != (len-1)){//Dan j bukan berada pada salah satu ujung tabel
                            //Maka terdapat 6 kotak yang diperiksa menggunakan for loop, dari (i-1, j-1) sampai (i, j+1)
                            for(int k = i-1; k <= i ; k++){
                                for(int p = j-1; p <= j +1; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }else if(j == 0){//Jika j berada pada ujung 0
                            //Maka terdapat 4 kotak yang diperiksa menggunakan for loop, dari (i-1, j) sampai (i, j+1)
                            for(int k = i-1; k <= i  ; k++){
                                for(int p = j; p <= j+1; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                               }
                            }
                        }else if(j == (len-1)){//Jika j berada pada ujung tabel bukan 0
                            //Maka terdapat 4 kotak yang diperiksa menggunakan for loop, dari (i-1, j-1) sampai (i, j)
                            for(int k = i-1; k <= i ; k++){
                                for(int p = j-1; p <= j; p++){
                                    if(arrGene[k*len + p] == 1){//Jika kotak yang diperiksa merupakan 1 (kotak hitam)
                                        counter++;//Maka tambahkan counter
                                    }
                                }
                            }
                        }
                    }
                    //Setelah mendapatkan banyaknya kotak hitam maka periksa bedanya dengan angka tabel yang sebenarnya
                    int beda = Math.abs(counter-tabel[((i*len) + j)]);//membuat variabel beda untuk menghitung selisih antara angka tabel yang benar dan banyaknya kotak hitam
                    this.fitness -= beda*(tabel[i*len+j]+1);//Mengurangi hasil selisih tersebut dikali dengan angka tabel + 1 (untuk menghindari angka 0), jika tidak ada perbedaan maka tidak ada pengurangan   
                }
            }
        }
    }
}
