/**
 * ThreadRandom
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of Web_Applic_Reservations.
 *
 * Web_Applic_Reservations is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Web_Applic_Reservations is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Web_Applic_Reservations; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package threads;

import java.util.Random;

/**
 * Manage a ThreadRandom
 * @author Sh1fT
 */
public class ThreadRandom extends Thread {
    private ThreadMonitor parent;
    private Integer number;
    private Integer random;
    private Boolean stop;

    /**
     * Create a new ThreadRandom instance
     * @param parent
     * @param number 
     */
    public ThreadRandom(ThreadMonitor parent, Integer number) {
        this.setParent(parent);
        this.setNumber(number);
        this.setRandom(null);
        this.setStop(false);
    }

    public ThreadMonitor getParent() {
        return parent;
    }

    public void setParent(ThreadMonitor parent) {
        this.parent = parent;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRandom() {
        return random;
    }

    public void setRandom(Integer random) {
        this.random = random;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    @Override
    public void run() {
        while (!this.getStop()) {
            while (!this.getParent().getNumberChoosen()[this.getNumber()]) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Error: " + ex.getLocalizedMessage());
                    this.setStop(true);
                    System.exit(1);
                }
            }
            this.setRandom(new Random().nextInt(4000));
            synchronized (this.getParent()) {
                this.getParent().notify();
            }
            this.getParent().getNumberChoosen()[this.getNumber()] = false;
        }
    }
}