/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Potocar;

import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author mino
 */
public class Algoritmus {

    private int maxTok = 0;
    private int kapacitaHrany[][];
    private int tokHrany[][];
    private int ideg[];
    private int odeg[];
    private int x[];
    private int zdroj;
    private int ustie;
    private boolean hrany[][];
    private ArrayList<Integer> zasobnik;

    public Algoritmus() throws IOException {
        java.io.File subor = new File("graf.txt");
        Scanner citac = new Scanner(subor);
        this.zasobnik = new ArrayList<Integer>();

        int temp = citac.nextInt();
        this.odeg = new int[temp];
        this.ideg = new int[temp];
        this.x = new int[temp];
        this.kapacitaHrany = new int[temp][temp];
        this.hrany = new boolean[temp][temp];
        this.tokHrany = new int[temp][temp];

        while (citac.hasNextLine()) {
            citac.nextLine();
            int temp1 = citac.nextInt();
            int temp2 = citac.nextInt();
            int kapacita = citac.nextInt();
            this.kapacitaHrany[temp1][temp2] = kapacita;
            this.hrany[temp1][temp2] = true;
            this.hrany[temp2][temp1] = true;
            this.ideg[temp2]++;
            this.odeg[temp1]++;
        }
        citac.close();
        for (int i = 0; i < this.x.length; i++) {
            if (this.ideg[i] == 0) {
                this.zdroj = i;
            }
            if (this.odeg[i] == 0) {
                this.ustie = i;
            }
        }

        boolean test = true;
        while (test) {
            boolean testNenajdenaZv = false;
            ArrayList<Integer> objaveneV = new ArrayList<Integer>();
            this.zasobnik.add(this.zdroj);
            for (int i = 0; i < this.x.length; i++) {
                if (this.ideg[i] != 0) {
                    this.x[i] = Integer.MAX_VALUE / 2;
                }
            }
            while (test) {
                int r = this.zasobnik.get(0);
                objaveneV.add(r);
                for (int i = 0; i < this.hrany.length; i++) {
                    if (this.hrany[r][i]) {
                        if (this.kapacitaHrany[r][i] != 0) {
                            if (this.tokHrany[r][i] < this.kapacitaHrany[r][i]) {
                                test = false;
                                for (Integer objaveny : objaveneV) {
                                    if (objaveny == i) {
                                        test = true;
                                        break;
                                    }
                                }
                                if (!test) {
                                    objaveneV.add(i);
                                    this.zasobnik.add(i);
                                    this.x[i] = r;
                                }
                            }
                        } else {
                            test = false;
                            for (Integer objaveny : objaveneV) {
                                if (objaveny == i) {
                                    test = true;
                                    break;
                                }
                            }
                            if (!test) {
                                objaveneV.add(i);
                                this.zasobnik.add(i);
                                this.x[i] = -r;
                            }
                        }
                    }
                }
                test = true;
                for (Integer objaveny : objaveneV) {
                    if (objaveny == ustie) {
                        test = false;
                        break;
                    }
                }
                this.zasobnik.remove((Integer) r);
                if (this.zasobnik.isEmpty()) {
                    test = false;
                    testNenajdenaZv = true;
                    System.out.println("Nenasla sa cesta");
                    break;
                }
            }
            this.zasobnik.clear();
            test = true;
            if (testNenajdenaZv) {
                test = false;
            } else {
                ArrayList<Integer> zvacsujucaPolocesta = new ArrayList<Integer>();
                int[][] rezervaHrany = new int[this.tokHrany.length][this.tokHrany.length];
                int rezerva = 0;
                int vrchol = this.ustie;
                while (test) {
                    zvacsujucaPolocesta.add(vrchol);
                    if (vrchol == Math.abs(zdroj)) {
                        break;
                    }
                    vrchol = this.x[Math.abs(vrchol)];
                }
                int najmensie = Integer.MAX_VALUE;
                for (int i = 0; i != zvacsujucaPolocesta.size() - 1; i++) {
                    if (zvacsujucaPolocesta.get(i) > 0) {
                        rezervaHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] = this.kapacitaHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] - this.tokHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))];
                    } else {
                        rezervaHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] = this.tokHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))];
                    }
                }
                for (int i = 0; i < rezervaHrany.length; i++) {
                    for (int j = 0; j < rezervaHrany.length; j++) {
                        if (rezervaHrany[i][j] != 0) {
                            najmensie = Math.min(najmensie, rezervaHrany[i][j]);
                        }
                    }
                }
                for (int i = 0; i != zvacsujucaPolocesta.size() - 1; i++) {
                    if (zvacsujucaPolocesta.get(i) > 0) {
                        this.tokHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] = this.tokHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] + najmensie;
                    } else {
                        this.tokHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] = this.tokHrany[this.x[Math.abs(zvacsujucaPolocesta.get(i))]][Math.abs(zvacsujucaPolocesta.get(i))] - najmensie;
                    }
                }
                this.maxTok = 0;
                for (int i = 0; i < this.tokHrany.length; i++) {
                    if (this.hrany[zdroj][i]) {
                        this.maxTok += this.tokHrany[zdroj][i];
                    }
                }
            }
            System.out.println("Do pi" + maxTok);
        }
        System.out.println("Hrana");
        for(int i = 0; i < this.hrany.length ; i++) {
            for (int j = 0; j < this.hrany.length ; j++) {
                if(this.hrany[i][j]) {
                    if(this.kapacitaHrany[i][j] != 0)
                    System.out.println(i + " " + j + " " + this.kapacitaHrany[i][j] + "/" + this.tokHrany[i][j]);
                }
            }
        }
        System.out.println("Maximalny tok je " + this.maxTok);
    }
}
