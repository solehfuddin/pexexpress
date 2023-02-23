package com.pex.pex_courier.helper

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.pex.pex_courier.R
import com.pex.pex_courier.dto.order.OrderDTO
import java.text.DecimalFormat
import java.text.NumberFormat


object Helper {
    // Server PROD
    var ApiURL = "https://pex.pexpress.my.id/public/index.php/api/"
    var imageURL = "https://pex.pexpress.my.id/public/uploads/"
    var publicURL = "https://pex.pexpress.my.id/public/"
    // Server DEV
//    var ApiURL = "https://dev.empatberkahglobal.id/public/index.php/api/"
//    var imageURL = "https://dev.empatberkahglobal.id/public/uploads/"
//    var ApiURL = "https://b5b2-180-250-96-155.ngrok.io/pex.pexpress.my.id/public/index.php/api/"
//    var imageURL = "https://b5b2-180-250-96-155.ngrok.io/pex.pexpress.my.id/public/uploads/"
//    var publicURL = "https://b5b2-180-250-96-155.ngrok.io/pex.pexpress.my.id/public/"

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
                        "[L]Patokan : ${data.alamatpenerima}\n"+
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

    fun printMultiResi(data: ArrayList<OrderDTO>,context:Context) {
        val printer =
            EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)

        data.forEach {
            if (it.statusdelivery == 1)
            {
                Log.d("Cetak Resi : ", it.nomortracking.toString())
                Log.d("Pengirim : ", it.namapengirim.toString())

                val formatter: NumberFormat = DecimalFormat("#,###")
                val myNumber = it.biaya?.toInt()
                val formattedNumber: String = formatter.format(myNumber)

                printer
                    .printFormattedText(
                        "[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.resources.getDrawableForDensity(
                            R.drawable.logo_print, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
                                "[C]<font size='medium'>${it.layanan}</font>\n" +
                                "[L]\n" +
                                "[C]<font size='medium'>${it.nomortracking}</font>\n"+
                                "[L]\n"+
                                "[L]Nama Pengirim : ${it.namapengirim}\n"+
                                "[L]Tanggal : ${it.tanggalpenugasanpickup} ${it.jampenugasanpickup}\n" +
                                //"[L]Asal : ${data.kecamatanpengirim}, ${data.kotapengirim}\n"+
                                "[L]Asal : \n"+
                                "[L]${it.gkecamatanpengirim}\n"+
                                "[L]Telpon : ${it.teleponpengirim}\n"+
                                "[L]\n"+
                                "[L]Penerima : ${it.namapenerima}\n"+
                                "[L]Telpon Penerima : ${it.teleponpenerima}\n"+
                                "[L]Alamat Penerima : \n"+
                                "[L]${it.gkecamatanpenerima}\n"+
                                "[L]Patokan : ${it.alamatpenerima}\n"+
                                "[L]\n"+
                                "[L]Info Barang & Tarif :\n"+
                                "[L]${it.jenisukuran} = ${it.maksimalberat} Kg\n"+
                                //"[L]${data.jenisbarang}\n"+
                                "[L]${it.namaJenisBarang}\n"+
                                "[L]Catatan : ${it.catatanpengirim}\n"+
                                //"[L]Tarif : ${tarif}\n"+
                                "[L]Tarif : ${"Rp $formattedNumber"}\n"+
                                "[L]\n"+
                                "[L]\n"+
                                "[C]<qrcode size='25'>${it.nomortracking}/</qrcode>\n"+
                                "[L]\n"+
                                "[L]<img>"+PrinterTextParserImg.bitmapToHexadecimalString(printer, generateBarcode(it.nomortracking.toString()))+"</img>\n"+
                                "[L]\n"+
                                "[L]\n"
                    )
            }
        }
    }
}