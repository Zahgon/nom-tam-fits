package nom.tam.fits.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import nom.tam.fits.FitsException;
/*
 * #%L
 * nom.tam FITS library
 * %%
 * Copyright (C) 1996 - 2024 nom-tam-fits
 * %%
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */
import static nom.tam.util.LoggerHelper.getLogger;

/**
 * (<i>for internal use</i>) Decompression of compressed FITS files of all supported types (<code>.gz</code>,
 * <code>.Z</code>, <code>.bz2</code>). It autodetects the type of compression used based on the first 2-bytes of the
 * compressed input stream. When possible, preference will be given to perform the decompression using a system command
 * (<code>uncompress</code> or <code>bzip2</code>, which are likely faster for large files). If such a tool is not
 * available, then the Apache <b>common-compress</b> classes will be used to the same effect.
 *
 * @see GZipCompressionProvider
 * @see BZip2CompressionProvider
 * @see ZCompressionProvider
 */
public final class CompressionManager {

    private static final String BZIP2_EXTENTION = ".bz2";

    private static final String COMPRESS_EXTENTION = ".Z";

    private static final String GZIP_EXTENTION = ".gz";

    /**
     * bytes in a megabyte (MB)
     */
    public static final int ONE_MEGABYTE = 1024 * 1024;

    /**
     * logger to log to.
     */
    private static final Logger LOG = getLogger(CompressionManager.class);

    private CompressionManager() {
    }

    /**
     * This method decompresses a compressed input stream. The decompression method is selected automatically based upon
     * the first two bytes read.
     *
     * @param  compressed    The compressed input stream
     *
     * @return               A stream which wraps the input stream and decompresses it. If the input stream is not
     *                           compressed, a pushback input stream wrapping the original stream is returned.
     *
     * @throws FitsException when the stream could not be read or decompressed
     */
    public static InputStream decompress(InputStream compressed) throws FitsException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Is a file compressed? (the magic number in the first 2 bytes is used to detect the compression.
     *
     * @param  file file to test for compression algorithms
     *
     * @return      true if the file is compressed
     */
    public static boolean isCompressed(File file) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * <p>
     * Checks if a file is compressed. If the file by the name exists, it will check the magic number in the first
     * 2-bytes to see if they matched those of the supported compression algorithms. Otherwise it checks if the file
     * extension atches one of the standard extensions for supported compressed files (<code>.gz</code>,
     * <code>.Z</code>, or <code>.bz2</code>).
     * </p>
     * <p>
     * As of 1.18, all file extension are checked in a case insensitive manner
     * </p>
     *
     * @param  filename of the file to test for compression algorithms
     *
     * @return          true if the file is compressed
     */
    public static boolean isCompressed(String filename) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private static ICompressProvider selectCompressionProvider(int mag1, int mag2) {
        return nextCompressionProvider(mag1, mag2, null);
    }

    /**
     * Returned the next highest priority decompression class, after the one we don't want, for the given type of
     * compressed file.
     *
     * @param  mag1 the first magic byte at the head of the compressed file
     * @param  mag2 the second magic byte at the head of the compressed file
     * @param  old  the last decompression class we tried for this type of file
     *
     * @return      the next lower priority decompression class we might use.
     */
    protected static ICompressProvider nextCompressionProvider(int mag1, int mag2, ICompressProvider old) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
