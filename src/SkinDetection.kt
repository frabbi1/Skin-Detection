import java.awt.Color
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import java.awt.image.BufferedImage



data class Pixels(var totalPixel: Int, var totalSkin: Int,var totalNonskin: Int )

fun main(args: Array<String>){
    var skin  = Array(256){Array(256){IntArray(256){0}}}
    var nonSkin = Array(256){Array(256){IntArray(256){0}}}

    var (totalPixel, totalSkin, totalNonSkin) =readDataset(skin,nonSkin)
    //println(totalPixel)
    //println("${skin[0][0][0]}")
    test(skin, nonSkin,totalPixel)


}

fun readDataset(skin: Array<Array<IntArray>>, nonSkin: Array<Array<IntArray>>): Pixels{



    var ds = File("data.txt").bufferedReader()

    for(i in 0 until 256){
        for( j in 0 until 256 ){
            for(k in 0 until 256){
                var st = ds.readLine()
                var lineList = st.split(" ")

                skin[i][j][k] = lineList[0].toInt()
                nonSkin[i][j][k] = lineList[1].toInt()
            }
        }
    }
    var st = ds.readLine().split(" ")
    //var totalPixel = st[0]
    ds.close()
    return Pixels(st[0].toInt(),st[1].toInt(),st[2].toInt())
}

fun test(skin: Array<Array<IntArray>>, nonSkin: Array<Array<IntArray>>, totalPixel: Int){
    var images = File("test/").list()
    var totalImg = images.size


    var pSkin =0
    for(i in 0 until totalImg){
        var file = File("test/${images[i]}")
        //var cpy = File("outMask/o$i.bmp")
        var cpy = file.copyTo(File("outMask/o$i.bmp"), true)

        var img = ImageIO.read(file)
        //var out = ImageIO.read(cpy)
        var w = img.width
        var h = img.height
        var out = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
        var r = 0
        var g = 0
        var b = 0
        var white = Color(255,255,255).rgb
        for(j in 0 until w){
            for(k in 0 until h){
                var clr = Color(img.getRGB(j,k))
                r = clr.red
                g = clr.green
                b = clr.blue

                var pSkinRGB = skin[r][g][b].toDouble()/totalPixel.toDouble()
                var pNonSkinRGB = nonSkin[r][g][b].toDouble()/totalPixel.toDouble()

                if(pNonSkinRGB  == 0.0){
                    if(pSkinRGB > 0.6) out.setRGB(j,k,clr.rgb)
                    else{
                        println("whitening")
                        out.setRGB(j,k,white)
                    }
                }
                else{
                    if(pSkinRGB/pNonSkinRGB > 0.35 ){
                        out.setRGB(j,k,clr.rgb)
                    }
                    else{
                        println("whitening")
                        out.setRGB(j,k,white)
                    }
                }
            }
        }
        ImageIO.write(out,"png",cpy)

    }
}
