package com.aljon.purrito.util

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class MediaHelperTest {

    @Test
    fun getFileExtension() {
        //Given a string url of file...
        val fileUrl = "https://cdn2.thecatapi.com/images/1gt.gif"
        val expected = "gif"

        //When getting it's file extension
        val result = MediaHelper.getFileExtension(fileUrl)

        assertThat(result, `is`(expected))
    }

    @Test
    fun isGif_ResultGif() {
        //Given a string url of file...
        val fileUrl = "https://cdn2.thecatapi.com/images/1gt.gif"

        //When getting it's file extension
        val result = MediaHelper.isGif(fileUrl)

        assertTrue(result)
    }

    @Test
    fun isGif_ResultJPEG() {
        //Given a string url of file...
        val fileUrl = "https://cdn2.thecatapi.com/images/1gt.jpeg"

        //When getting it's file extension
        val result = MediaHelper.isGif(fileUrl)

        assertFalse(result)
    }
}