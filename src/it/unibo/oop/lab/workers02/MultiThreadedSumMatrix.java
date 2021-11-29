/**
 * 
 */
package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nThreads;

    /**
     * @param n
     */
    public MultiThreadedSumMatrix(final int n) {
        super();
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.nThreads = n;
    }

    private final class Worker extends Thread {

        private final double[][] matrix;
        private final int startPos;
        private final int nElem;
        private long res;

        /**
         * @param matrix
         * @param startPos
         * @param nElem
         */
        Worker(double[][] matrix, int startPos, int nElem) {
            super();
            this.matrix = matrix;
            this.startPos = startPos;
            this.nElem = nElem;
        }

        @Override
        public void run() {
            for (int i = startPos; i < matrix.length && i < startPos + nElem; i++) {
                for (final var d : this.matrix[i]) {
                    this.res += d;
                }
            }
        }

        /**
         * @return the res
         */
        public long getRes() {
            return this.res;
        }

    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length / nThreads + matrix.length % nThreads;

        final List<Worker> workers = new ArrayList<>(nThreads);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (final Thread d : workers) {
            d.start();
        }

        double sumMatrix = 0;
        for (final Worker w : workers) {
            try {
                w.join();
                sumMatrix += w.getRes();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sumMatrix;
    }

}
