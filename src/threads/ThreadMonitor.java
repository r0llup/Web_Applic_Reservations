/**
 * ThreadMonitor
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

import applets.AppletHolidaysLogin;
import java.util.ArrayList;

/**
 * Manage a {@link ThreadMonitor}
 * @author Sh1fT
 */
public class ThreadMonitor extends Thread {
    private AppletHolidaysLogin parent;
    private ArrayList<ThreadRandom> trs;
    private Boolean stop;
    private Boolean [] numberChoosen = {false, false, false};
    private Integer sumRandom;
    private Integer luckyNumber;

    /**
     * Create a new {@link ThreadMonitor} instance
     * @param parent 
     */
    public ThreadMonitor(AppletHolidaysLogin parent) {
        this.setParent(parent);
        this.setTrs(new ArrayList<ThreadRandom>());
        this.setStop(false);
        this.setSumRandom(0);
        this.setLuckyNumber(5);
    }

    /**
     * Initialize the random's threads
     */
    public void init() {
        for (Integer i = 0; i < 3; i++)
            this.getTrs().add(new ThreadRandom(this, i));
    }

    public AppletHolidaysLogin getParent() {
        return parent;
    }

    public void setParent(AppletHolidaysLogin parent) {
        this.parent = parent;
    }

    public ArrayList<ThreadRandom> getTrs() {
        return trs;
    }

    public void setTrs(ArrayList<ThreadRandom> trs) {
        this.trs = trs;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public Boolean[] getNumberChoosen() {
        return numberChoosen;
    }

    public void setNumberChoosen(Boolean[] numberChoosen) {
        this.numberChoosen = numberChoosen;
    }

    public Integer getSumRandom() {
        return sumRandom;
    }

    public void setSumRandom(Integer sumRandom) {
        this.sumRandom = sumRandom;
    }

    public Integer getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(Integer luckyNumber) {
        this.luckyNumber = luckyNumber;
    }

    /**
     * Launch the random's threads
     */
    public void launch() {
        for (ThreadRandom tr : this.getTrs()) {
            tr.start();
        }
    }

    @Override
    public void run() {
        while (!this.getStop()) {
            try {
                for (Integer i = 0; i < 3; i++) {
                    this.getNumberChoosen()[i] = true;
                    synchronized (this.getTrs().get(i)) {
                        this.getTrs().get(i).notify();
                    }
                }
                while (this.getTrs().get(0).getRandom() == null ||
                        this.getTrs().get(1).getRandom() == null ||
                        this.getTrs().get(2).getRandom() == null) {
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
                this.setSumRandom(this.getTrs().get(0).getRandom()+
                        this.getTrs().get(1).getRandom()+
                        this.getTrs().get(2).getRandom());
                if ((this.getSumRandom() % this.getLuckyNumber()) == 0)
                    this.getParent().getChanceLabel().setText("Réduction de 5%");
                else
                    this.getParent().getChanceLabel().setText("Aucune réduction disponible");
                for (Integer i = 0; i < 3; i++)
                    this.getTrs().get(i).setRandom(null);
                this.sleep(30000);
            } catch (InterruptedException ex) {
                System.out.println("Error: " + ex.getLocalizedMessage());
                this.setStop(true);
                System.exit(1);
            }
        }
    }
}