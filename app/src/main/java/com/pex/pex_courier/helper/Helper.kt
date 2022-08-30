package com.pex.pex_courier.helper

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO


object Helper {
    // Server PROD
    var ApiURL = "https://pex.pexpress.my.id/public/index.php/api/"
    var imageURL = "https://pex.pexpress.my.id/public/uploads/"
    // Server DEV
    //var ApiURL = "https://0409-180-250-96-155.ngrok.io/pex.pexpress.my.id/public/index.php/api/"
    //var imageURL = "https://0409-180-250-96-155.ngrok.io/pex.pexpress.my.id/public/uploads/"

    fun generateBarcode(codeTracking: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(codeTracking, BarcodeFormat.CODE_128, 500, 100)
    }

    fun printResi(data: OrderDTO,context:Context,tarif:String,jenisUkuran:String) {

        val printer =
            EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
        printer
            .printFormattedText(
                "[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.resources.getDrawableForDensity(
                    R.drawable.logo_print, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                        "[C]<font size='medium'>${data.layanan}</font>\n" +
                        "[L]\n" +
                        "[C]<font size='medium'>${data.nomortracking}</font>\n"+
                        "[L]\n"+
                        "[L]Nama Pengirim : ${data.namapengirim}\n"+
                        "[L]Tanggal : ${data.tanggalpenugasanpickup} ${data.jampenugasanpickup}\n" +
                        "[L]Asal : ${data.kecamatanpengirim}, ${data.kotapengirim}\n"+
                        "[L]Telpon : ${data.teleponpengirim}\n"+
                        "[L]\n"+
                        "[L]Penerima : ${data.namapenerima}\n"+
                        "[L]Telpon Penerima : ${data.teleponpenerima}\n"+
                        "[L]Alamat Penerima : \n"+
                        "[L]${data.gkecamatanpenerima}\n"+
                        "[L]\n"+
                        "[L]Info Barang & Tarif :\n"+
                        "[L]${jenisUkuran} = ${data.maksimalberat} Kg\n"+
                        "[L]${data.jenisbarang}\n"+
                        "[L]Catatan : ${data.catatanpengirim}\n"+
                        "[L]Tarif : ${tarif}\n"+
                        "[L]\n"+
                        "[L]\n"+
                        "[C]<qrcode size='20'>${data.nomortracking}/</qrcode>\n"+
                        "[L]\n"+
                        "[L]<img>"+PrinterTextParserImg.bitmapToHexadecimalString(printer, generateBarcode(data.nomortracking.toString()))+"</img>\n"+
                        "[L]\n"+
                        "[L]\n"

            )
    }
}