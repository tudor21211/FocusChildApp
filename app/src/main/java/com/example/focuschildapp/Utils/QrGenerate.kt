package com.example.focuschildapp.com.example.focuschildapp.Utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.io.ByteArrayOutputStream
import java.util.Hashtable

class QrGenerate {
    companion object {
        fun generateQRCodeBitmap(byteArray: ByteArray): Bitmap? {
            try {
                val text = byteArray.toString(Charsets.UTF_8)

                val hintsQR: MutableMap<EncodeHintType, Any> = Hashtable()
                hintsQR[EncodeHintType.CHARACTER_SET] = "UTF-8"
                hintsQR[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
                val writer = QRCodeWriter()

                val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512, hintsQR)

                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(
                            x,
                            y,
                            if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                        )
                    }
                }

                return bmp
            } catch (e: WriterException) {
                e.printStackTrace()
            }

            return null
        }

        fun drawableToByteString(drawable: Drawable): String? {
            val bitmap = drawableToBitmap(drawable)

            val byteArray = bitmapToByteArray(bitmap)

            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        private fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }

        private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }


        fun byteStringToDrawable(byteString: String): Drawable {
            val byteArray = Base64.decode(byteString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            return BitmapDrawable(null, bitmap)
        }

    }
}