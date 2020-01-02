/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlcalidad;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author bryan
 */
public class main{
    static Color arregloImagen[][];
    static Color arregloImagenCopia[][];
    static int anchoImagen;
    static int altoImagen;
    static File archivoImagenVerdes;
    static File archivoImagenMancha;
    static boolean imagenResultado;    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ventanaPrincipal().setVisible(true);
    }
    public static void cargarImagen(String ruta) throws IOException{
        File imagen = new File(ruta);
        BufferedImage bf=null;
        bf = ImageIO.read(imagen);
        anchoImagen = bf.getWidth();
        altoImagen = bf.getHeight();
        arregloImagen = new Color[altoImagen][anchoImagen];
        arregloImagenCopia = new Color[altoImagen][anchoImagen];
        for (int i = 0; i < altoImagen; i++) {
            for (int j = 0; j < anchoImagen; j++) {
                arregloImagen[i][j]= new Color(bf.getRGB(j,i));
                arregloImagenCopia[i][j] = arregloImagen[i][j];
            }
        }
        binarizacionImagenVerdes(20);//antes: 50
        imprimirImagen("ImagenVerdes.jpg");
        binarizacionImagenManchas(50);
        imprimirImagen("ImagenManchas.jpg");
        perceptron(promedioMatrizVerde(),promedioMatrizMancha());
    } 
    public static void binarizacionImagenVerdes(double umbral){
        for (int i = 0; i < altoImagen; i++) {
            for (int j = 0; j < anchoImagen; j++) {
                Color pix= arregloImagen[i][j];
                int promedio =(pix.getGreen()-pix.getRed()-pix.getBlue());//Determina el color verde como predominante
                if (promedio<umbral){ 
                    arregloImagen[i][j]=Color.BLACK;
                }else{
                    arregloImagen[i][j] = Color.WHITE;
                }
            }
        }        
    }
    public static void binarizacionImagenManchas(double umbral){
        for (int i = 0; i < altoImagen; i++) {
            for (int j = 0; j < anchoImagen; j++) {
                Color pix= arregloImagenCopia[i][j];
                int promedio =(pix.getGreen()+pix.getRed()+pix.getBlue());//Determina el color negro como predominante
                if (promedio<umbral){ 
                    arregloImagenCopia[i][j]=Color.BLACK;
                }else{
                    arregloImagenCopia[i][j] = Color.WHITE;
                }
            }
        }        
    }    
    public static void imprimirImagen(String nombreImagen) throws IOException{
        BufferedImage salida = new BufferedImage(anchoImagen, altoImagen, BufferedImage.TYPE_INT_RGB);
        switch (nombreImagen) {
            case "ImagenVerdes.jpg":
                for (int i = 0; i < altoImagen; i++) {
                    for (int j = 0; j < anchoImagen; j++) {
                        salida.setRGB(j, i, arregloImagen[i][j].getRGB());
                    }
                }
                archivoImagenVerdes = new File(nombreImagen);
                break;
            case "ImagenManchas.jpg":
                for (int i = 0; i < altoImagen; i++) {
                    for (int j = 0; j < anchoImagen; j++) {
                        salida.setRGB(j, i, arregloImagenCopia[i][j].getRGB());
                    }
                }                
                archivoImagenMancha = new File(nombreImagen);
                break;
            default:
                System.out.println("No se puede");
                break;
        }
        ImageIO.write(salida,"jpg",new File(nombreImagen));
    }
    public static double promedioMatrizVerde(){
        double resultado = 0.0;
        for (int i = 0; i < altoImagen; i++) {
            for (int j = 0; j < anchoImagen; j++) {
                if(255 == arregloImagen[i][j].getBlue() && 255 == arregloImagen[i][j].getGreen() && 255 == arregloImagen[i][j].getRed()){
                    int srcPixel = arregloImagen[i][j].getRGB();
                    resultado += srcPixel;
                }
            }
        }
        resultado /= altoImagen*anchoImagen;
        return Math.abs(resultado);
    }
    public static double promedioMatrizMancha(){
        double resultado = 0.0;
        for (int i = 0; i < altoImagen; i++) {
            for (int j = 0; j < anchoImagen; j++) {
                if(255 == arregloImagenCopia[i][j].getBlue() && 255 == arregloImagenCopia[i][j].getGreen() && 255 == arregloImagenCopia[i][j].getRed()){
                    int srcPixel = arregloImagenCopia[i][j].getRGB();
                    resultado += srcPixel;
                }
            }
        }
        resultado /= altoImagen*anchoImagen;
        return Math.abs(resultado);
    }
    
    public static String impresionResultados(){
        String cadena = "\tImagen Escaneada\n\n"+
                        "Porcentaje de verde: "+promedioMatrizVerde()+"\n"+
                        "Porcentaje de manchas: "+promedioMatrizMancha()+"\n"+
                        "Resolucion: "+anchoImagen+" x "+altoImagen+" px";
        return cadena;
    }
    /*PERCEPTRON*/
    public static float funcion(float d){
	return d > 0 ? 1 : 0;
    }
    public static float neurona(float entrada1,float entrada2,float peso1,float peso2, float umbral){
	return umbral + entrada1*peso1 + entrada2*peso2;
    }
    public static double getRandom(Double valorMinimo, Double valorMaximo) {
        Random rand = new Random();
        return  valorMinimo + ( valorMaximo - valorMinimo ) * rand.nextDouble();
    }
    public static void perceptron(double entrada1,double entrada2){
        float peso1 = 0, peso2 = 0;
        float umbral = 0;
        boolean sw = false;
        while(!sw){
                sw = true;
                peso1 = (float)(getRandom(0.0, 10.0)) - (float)(getRandom(0.0, 10.0));
                peso2 = (float)(getRandom(0.0, 10.0)) - (float)(getRandom(0.0, 10.0));
                umbral = (float)(getRandom(0.0, 10.0)) - (float)(getRandom(0.0, 10.0));
                
                if (funcion(neurona((float)0.0,(float)0.66,peso1,peso2,umbral)) !=0){ //malo
                        sw = false; 
                }
                if (funcion(neurona((float)0.016,(float)0.6072,peso1,peso2,umbral)) !=0){	//malo
                        sw = false;	
                }		
                if (funcion(neurona((float)0.20,(float)0.69,peso1,peso2,umbral)) !=0){ //malo
                        sw = false; 
                }
                if (funcion(neurona((float)0.0,(float)0.651,peso1,peso2,umbral)) !=1){ //bueno
                        sw = false; 
                }
                if (funcion(neurona((float)0.0,(float)0.659,peso1,peso2,umbral)) !=1){ //bueno
                        sw = false; 
                }
                if (funcion(neurona((float)0.003,(float)0.60,peso1,peso2,umbral)) !=1){ //bueno
                        sw = false; 
                }
        }
        if(funcion(neurona((float)entrada1,(float)entrada2,peso1,peso2,umbral)) == 0){
                        imagenResultado = true;
                }else{
                        imagenResultado = false;
                }
    }
}
