package version1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * PROGRAMA JUEGO DE LA VIDA
 *
 * @author Marta Muñoz San Román
 *
 */

public class JuegoVida {

    // ************ VARIABLES ***********************

    static final int SIZE = 20;
    // 'genesis' es el estado incial de la matriz (gen 0), que ira cambiando con
    // cada nueva generacion
    static int[][] genesis = new int[SIZE][SIZE];

    // CELULAS: en la matriz, una muerta tiene 0 y una viva un 1
    static final char MUERTA = 0;
    static final char VIVA = 1;

    // el juego de la vida cambia con cada generacion, esta variable guarda el cambio de cada generacion
    static int generacion = 20;
    //separador que se va a usar a la hora de leer el archivo, que es un archivo separado por comas (csv)
    static final String SEPARADOR = ",";


    /**
     * METODO QUE LEE ARCHIVO CSV Y LO CARGA
     * Esta informacion sera la que se cargara incialmente
     * (Codigo copiado del aula virtual de programacion)
     *
     * @param RUTA_LECTURA
     *
     */

    public static void cargaCSV(String RUTA_LECTURA) {


        try (BufferedReader lector = new BufferedReader
                (new FileReader(RUTA_LECTURA))) {

            // variables
            String linea;
            String[] fila;
            int numFila = 0;

            while ((linea = lector.readLine()) != null) {

                fila = linea.split(SEPARADOR);
                for (int i = 0; i < SIZE; i++) {
                    if (i < fila.length) {
                        genesis[numFila][i] = Integer.parseInt(fila[i].trim());
                    } else {
                        genesis[numFila][i] = 0;
                    }

                }
                numFila++;

            }
            // segun el ejercicio, una vez se recorran todas las filas el resto se rellenan con ceros
            if (numFila < SIZE) {
                for (int i = numFila; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        genesis[i][j] = 0;
                    }

                }
            }
            lector.close();

        } catch (IOException e) {
            System.out.println("Se produjo el siguiente ERROR al acceder al fichero: \n " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Revise el fichero; hay VALORES que no pueden convertirse a ENTEROS. El ERROR es: \n "
                    + e.getMessage());
        }

    }//FIN metodo cargaCSV, fin del metodo


    /**
     * METODO QUE ESCRIBE FICHERO
     * Tras leer el archivo CSV, saldra como salida un fichero con las generaciones que han pasado
     * (Codigo copiado del aula virtual de programacion)
     *
     * @param valores
     * @param RUTA_ESCRITURA
     */
    static boolean escribirFichero(int[][] valores, String RUTA_ESCRITURA) {

        String linea;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(RUTA_ESCRITURA))) {
            //metodo para abrir el fichero de forma destructiva
            //se recorre para ello por filas
            for (int i = 0; i < valores.length; i++) {
                linea = "";
                for (int k = 0; k < valores[i].length - 1; k++) {
                    linea = linea + Integer.toString(valores[i][k]) + SEPARADOR;
                }
                //se elimina la cadena de la ultima coma
                linea = linea + Integer.toString(valores[i][valores[i].length - 1]);
                //se agrega la nueva linea al fichero con un salto de linea
                escritor.write(linea);
                //si no esta en la ultima linea...
                if (i != valores.length - 1) {
                    escritor.newLine();
                }
            }
            return true;

        } catch (IOException e) {
            System.out.println("Se produjo el siguiente error al acceder al fichero \n"
                    + e.getMessage());
            return false;
        }
    }//fin metodo escritura, fin del metodo copiado

    /**
     * METODO que imprime una matriz inicial con caracteres
     * Una vez cargue el archivo, va a sustituir los 0s y 1s por caracteres
     *
     */
    public static void imprimeMatriz() {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // si encuentra un 1, se escribe con un '+'
                if (genesis[i][j] == VIVA) {
                    System.out.print(" + ");
                    // de lo contrario, se escribe con un '-'
                } else {
                    System.out.print(" - ");
                }

            }
            //SALTO DE LINEA para que no se junten
            System.out.println();
        }

    }//fin metodo


    /**
     * METODO que determina el numero de celulas que se encuentran dentro del
     * tablero. Devuelve el numero de celulas vivas que hay, una vez se han contado y
     * recorrido, las cuales estan adyacenes a una celda
     *
     * @param fila
     * @param columna
     * @return celViva
     */
    public static int cuentaVecinas(int fila, int columna) {

        int celViva = 0; //indica celula viva
        int aFila, zFila;
        int aCol, zCol;

        //RECORRIDO TABLERO
        //primero las filas
        if (fila == 0) {
            aFila = 0; zFila = 1;
        } else if (fila == (SIZE - 1)) {
            aFila = SIZE - 2; zFila = SIZE - 1;
        } else {
            aFila = fila - 1;
            zFila = fila + 1;
        }
        //luego las columnas
        if (columna == 0) {
            aCol = 0; zCol = 1;
        } else if (columna == (SIZE - 1)) {
            aCol = SIZE - 2; zCol = SIZE - 1;
        } else {
            aCol = columna - 1; zCol = columna + 1;
        }
        //se recorre el tablero una vez delimitados los comienzos y finales de las filas y las columnas
        for (int i = aFila; i <= zFila; i++) {
            for (int j = aCol; j <= zCol; j++) {
                if (!(i == fila && j == columna) && genesis[i][j] == 1) {
                    celViva++;
                }
            }

        }

        return celViva;

    }


    /**
     * METODO que decide si una celula vive, nace o muere,
     * De acuerdo con las reglas del Juego de la Vida, es decir, actualiza el tablero
     * He eliminado el teclado porque me daba muchos problemas...
     *
     * @param generacion
     * @return generacion nueva una vez aplicadas las reglas del juego
     */

    public static int[][] nextGen(int[][] generacion) {

        int[][]matrizFinal = new int [SIZE][SIZE];

        // recorre las celulas
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int vecinasVivas = cuentaVecinas(x, y);

                // si la celda esta viva y...
                // si es mayor o tiene 2 vecinos y es mayor de 3 o tiene 3 vecinos, sobrevive
                if (generacion[x][y] == VIVA
                        && (vecinasVivas == 2 || vecinasVivas ==3)) {
                    matrizFinal[x][y] = VIVA;
                } else {
                    matrizFinal[x][y] = MUERTA;
                }
                // si esta muerta, pero esta rodeada por 3 celulas, nace por gen. espontanea

                if (generacion[x][y] == MUERTA
                        && vecinasVivas == 3) {
                    matrizFinal[x][y] = VIVA;
                }
            }
        }
        return matrizFinal;

    }// fin metodo


    public static void main(String[] args) {

        //ahora el archivo se carga desde una carpeta del proyecto
        String RUTA_LECTURA = "CSVs/bloque1.txt";
        //ruta de escritura
        String RUTA_ESCRITURA = "CSVs/matrizVidaFinal.txt";
        cargaCSV(RUTA_LECTURA);

        System.out.println("*** Estado inicial de la matriz (comienza en 0)");
        // esta es la matriz inicial

        //se recorren las generaciones
        for (int i = 0; i < generacion ; i++) {

            System.out.println("Generacion numero..." + i);
            imprimeMatriz();


            escribirFichero(genesis, RUTA_ESCRITURA);

            // actualiza la matriz
            genesis = nextGen (genesis);


        }




    }

}//fin de la clase

