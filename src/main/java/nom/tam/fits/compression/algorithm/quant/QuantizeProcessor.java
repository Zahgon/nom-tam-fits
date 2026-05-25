package nom.tam.fits.compression.algorithm.quant;

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
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import nom.tam.fits.compression.algorithm.api.ICompressor;

/**
 * (<i>for internal use</i>) Qunatization step processor as part of compression.
 */
@SuppressWarnings({ "javadoc", "deprecation" })
public class QuantizeProcessor {

    public static class DoubleQuantCompressor extends QuantizeProcessor implements ICompressor<DoubleBuffer> {

        private final ICompressor<IntBuffer> postCompressor;

        public DoubleQuantCompressor(QuantizeOption quantizeOption, ICompressor<IntBuffer> compressor) {
            super(quantizeOption);
            postCompressor = compressor;
        }

        @Override
        public boolean compress(DoubleBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, DoubleBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    /**
     * TODO this is done very inefficient and should be refactored!
     */
    public static class FloatQuantCompressor extends QuantizeProcessor implements ICompressor<FloatBuffer> {

        private final ICompressor<IntBuffer> postCompressor;

        public FloatQuantCompressor(QuantizeOption quantizeOption, ICompressor<IntBuffer> postCompressor) {
            super(quantizeOption);
            this.postCompressor = postCompressor;
        }

        @Override
        public boolean compress(FloatBuffer buffer, ByteBuffer compressed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        public void decompress(ByteBuffer compressed, FloatBuffer buffer) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private class BaseFilter extends PixelFilter {

        BaseFilter() {
            super(null);
        }

        @Override
        protected void nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected double toDouble(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int toInt(double pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private class DitherFilter extends PixelFilter {

        private static final int RANDOM_MULTIPLICATOR = 500;

        private int iseed = 0;

        private int nextRandom = 0;

        DitherFilter(long seed) {
            super(null);
            initialize(seed);
        }

        public void initialize(long ditherSeed) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        private void initI1() {
            nextRandom = (int) (RandomSequence.get(iseed) * RANDOM_MULTIPLICATOR);
        }

        public double nextRandom() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected void nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected double toDouble(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int toInt(double pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private static class NullFilter extends PixelFilter {

        private final double nullValue;

        private final boolean isNaN;

        private final int nullValueIndicator;

        NullFilter(double nullValue, int nullValueIndicator, PixelFilter next) {
            super(next);
            this.nullValue = nullValue;
            isNaN = Double.isNaN(this.nullValue);
            this.nullValueIndicator = nullValueIndicator;
        }

        public final boolean isNull(double pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected double toDouble(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int toInt(double pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private static class PixelFilter {

        private final PixelFilter next;

        protected PixelFilter(PixelFilter next) {
            this.next = next;
        }

        protected void nextPixel() {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        protected double toDouble(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        protected int toInt(double pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private static class ZeroFilter extends PixelFilter {

        ZeroFilter(PixelFilter next) {
            super(next);
        }

        @Override
        protected double toDouble(int pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }

        @Override
        protected int toInt(double pixel) {
            throw new UnsupportedOperationException("STUB: not implemented");
        }
    }

    private static final double MAX_INT_AS_DOUBLE = Integer.MAX_VALUE;

    /**
     * number of reserved values, starting with
     */
    private static final long N_RESERVED_VALUES = 10;

    private static final double ROUNDING_HALF = 0.5;

    /**
     * value used to represent zero-valued pixels
     */
    private static final int ZERO_VALUE = Integer.MIN_VALUE + 2;

    private final boolean centerOnZero;

    private final PixelFilter pixelFilter;

    private double bScale;

    private double bZero;

    private Quantize quantize;

    protected final QuantizeOption quantizeOption;

    public QuantizeProcessor(QuantizeOption quantizeOption) {
        this.quantizeOption = quantizeOption;
        bScale = quantizeOption.getBScale();
        bZero = quantizeOption.getBZero();
        PixelFilter filter = null;
        boolean localCenterOnZero = quantizeOption.isCenterOnZero();
        if (quantizeOption.isDither2()) {
            filter = new DitherFilter(quantizeOption.getSeed() + quantizeOption.getTileIndex());
            localCenterOnZero = true;
            quantizeOption.setCheckZero(true);
        } else if (quantizeOption.isDither()) {
            filter = new DitherFilter(quantizeOption.getSeed() + quantizeOption.getTileIndex());
        } else {
            filter = new BaseFilter();
        }
        if (quantizeOption.isCheckZero()) {
            filter = new ZeroFilter(filter);
        }
        if (quantizeOption.isCheckNull()) {
            final NullFilter nullFilter = new NullFilter(quantizeOption.getNullValue(), quantizeOption.getBNull(), filter);
            filter = nullFilter;
            quantize = new Quantize(quantizeOption) {

                @Override
                protected int findNextValidPixelWithNullCheck(int nx, DoubleArrayPointer rowpix, int ii) {
                    throw new UnsupportedOperationException("STUB: not implemented");
                }

                @Override
                protected boolean isNull(double d) {
                    throw new UnsupportedOperationException("STUB: not implemented");
                }
            };
        } else {
            quantize = new Quantize(quantizeOption);
        }
        pixelFilter = filter;
        centerOnZero = localCenterOnZero;
    }

    public Quantize getQuantize() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public boolean quantize(double[] doubles, IntBuffer quants) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void quantize(final DoubleBuffer fdata, final IntBuffer intData) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public void unquantize(final IntBuffer intData, final DoubleBuffer fdata) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private void calculateBZeroAndBscale() {
        bScale = quantizeOption.getBScale();
        bZero = zeroCenter();
        quantizeOption.setIntMinValue(nint((quantizeOption.getMinValue() - bZero) / bScale));
        quantizeOption.setIntMaxValue(nint((quantizeOption.getMaxValue() - bZero) / bScale));
        quantizeOption.setBZero(bZero);
    }

    private int nint(double x) {
        return x >= 0. ? (int) (x + ROUNDING_HALF) : (int) (x - ROUNDING_HALF);
    }

    private double zeroCenter() {
        final double minValue = quantizeOption.getMinValue();
        final double maxValue = quantizeOption.getMaxValue();
        double evaluatedBZero;
        if (!quantizeOption.isCheckNull() && !centerOnZero) {
            // don't have to check for nulls
            // return all positive values, if possible since some compression
            // algorithms either only work for positive integers, or are more
            // efficient.
            if ((maxValue - minValue) / bScale < MAX_INT_AS_DOUBLE - N_RESERVED_VALUES) {
                evaluatedBZero = minValue;
                // fudge the zero point so it is an integer multiple of bScale
                // This helps to ensure the same scaling will be performed if
                // the file undergoes multiple fpack/funpack cycles
                long iqfactor = (long) (evaluatedBZero / bScale + ROUNDING_HALF);
                evaluatedBZero = iqfactor * bScale;
            } else {
                /* center the quantized levels around zero */
                evaluatedBZero = (minValue + maxValue) / 2.;
            }
        } else {
            // data contains null values or has be forced to center on zero
            // shift the range to be close to the value used to represent null
            // values
            evaluatedBZero = minValue - bScale * (Integer.MIN_VALUE + N_RESERVED_VALUES + 1);
        }
        return evaluatedBZero;
    }
}
