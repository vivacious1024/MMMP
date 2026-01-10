package com.kk.mmmp.runcode

import android.content.Context
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

//因为python不能直接访问assets文件夹中的文件，所以需要先用一个函数将其中的文件复制到应用的内部存储空间再用
//这个函数将文件放在"/data/data/com.kk.runcode/files/文件名" 目录下
fun copyFileFromAssetsToInternalStorage(context: Context, assetFileName: String, outputFileName: String){
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    try {
        inputStream = context.assets.open(assetFileName)
        val outFile = context.getFileStreamPath(outputFileName)
        outputStream = FileOutputStream(outFile)
        copyStream(inputStream, outputStream)
        outputStream.flush()
    }catch (e: IOException){
        e.printStackTrace()
    }finally {
        try {
            inputStream?.close()
            outputStream?.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}

@Throws(IOException::class)
private fun copyStream(inputStream: InputStream, outputStream: OutputStream){
    val buffer = ByteArray(1024)
    var read: Int
    while (inputStream.read(buffer).also { read = it } != -1){
        outputStream.write(buffer, 0, read)
    }
}