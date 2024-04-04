package com.example.focuschildapp.com.example.focuschildapp.Utils

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.Hashtable

class QrGenerate {
    companion object {
        fun generateQRCodeBitmap(byteArray: ByteArray): Bitmap? {
            try {
                val text = byteArray.toString(Charsets.UTF_8)

                val hints: MutableMap<EncodeHintType, Any> = Hashtable()
                hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
                hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L

                val writer = QRCodeWriter()

                val bitMatrix: BitMatrix =
                    writer.encode(text, BarcodeFormat.QR_CODE, 512, 512, hints)

                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(
                            x,
                            y,
                            if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                        )
                    }
                }

                return bmp
            } catch (e: WriterException) {
                e.printStackTrace()
            }

            return null
        }

    }
}