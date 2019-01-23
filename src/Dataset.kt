import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileWriter
import javax.imageio.ImageIO

fun main(args: Array<String>){
    makeDataset()
}

fun makeDataset(){
    var f1 = File("ibtd/").list()
    var f2 = File("Mask/"). list()

    //println(f1.size)
    //println(f2.size)

    var picSize = f1.size-1
    var skin = Array(256){Array(256){IntArray(256){0}}}
    var nonSkin = Array(256){Array(256){IntArray(256){0}}}
    var totalSkin = 0
    var totalNonSkin = 0
    var totalPixel = 0
    var r = 0
    var g = 0
    var b = 0

    for (i in 0 until picSize){
        println(i)
        var file = File("ibtd/${f1[i]}")
        var imgFile: BufferedImage = ImageIO.read(file)
        file = File("Mask/${f2[i]}")
        var maskFile: BufferedImage = ImageIO.read(file)

        var w = imgFile.width
        var h = imgFile.height

        totalPixel += (w*h)

        for( j in 0 until w){
            for( k in 0 until h){
                //totalPixel++
                var imgC = Color(imgFile.getRGB(j,k))
                var mskC = Color(maskFile.getRGB(j,k))
                r = imgC.red
                b = imgC.blue
                g = imgC.green

                if(mskC.red > 250 && mskC.green > 250 && mskC.blue > 250){
                    nonSkin[r][g][b]++
                    totalNonSkin++
                }
                else{
                    skin[r][g][b]++
                    totalSkin++
                }
            }
        }


    }


    var dat = FileWriter("data.txt")
    for(i in 0 until 256){
        for( j in 0 until 256 ){
            for(k in 0 until 256){
                dat.write("${skin[i][j][k]} ${nonSkin[i][j][k]}\n")
            }
        }
    }
    dat.write("$totalPixel $totalSkin $totalNonSkin")
    dat.close()




}