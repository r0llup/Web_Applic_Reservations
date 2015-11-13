/**
 * ThreadCalendar
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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JLabel;

/**
 * Manage a {@link ThreadCalendar}
 * @author Sh1fT
 */
public class ThreadCalendar extends Thread {
    private AppletHolidaysLogin parent;
    private JLabel label;
    private String country;
    private Locale locale;
    private Calendar calendar;
    private Boolean stop;

    /**
     * Create a new {@link ThreadCalendar} instance
     * @param parent
     * @param country
     */
    public ThreadCalendar(AppletHolidaysLogin parent, String country) {
        this.setParent(parent);
        this.setLabel(null);
        this.setCountry(country);
        this.setLocale(null);
        this.setCalendar(null);
        this.setStop(false);
    }

    /**
     * Initialize the locales and the corresponding labels
     */
    public void init() {
        switch (this.getCountry()) {
            case "BE":
                this.setLabel(this.getParent().getBeLabel());
                this.setLocale(Locale.FRENCH);
                break;
            case "US":
                this.setLabel(this.getParent().getUsLabel());
                this.setLocale(Locale.US);
                break;
            case "JP":
                this.setLabel(this.getParent().getJpLabel());
                this.setLocale(Locale.JAPAN);
                break;
        }
    }

    public AppletHolidaysLogin getParent() {
        return parent;
    }

    public void setParent(AppletHolidaysLogin parent) {
        this.parent = parent;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    /**
     * Return a localized date
     * @param dfd
     * @param dft
     * @return 
     */
    public String getLocalizedDate(int dfd, int dft) {
        DateFormat df = DateFormat.getDateTimeInstance(dfd, dft, this.getLocale());
         switch (this.getCountry()) {
             case "BE":
                 df.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
                 break;
             case "US":
                 df.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                 break;
             case "JP":
                 df.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
                 break;
         }
         return df.format(this.getCalendar().getTime());
    }

    @Override
    public void run() {
        while (!this.getStop()) {
            try {
                this.setCalendar(Calendar.getInstance());
                this.getLabel().setText(this.getLocalizedDate(DateFormat.LONG,
                        DateFormat.LONG));
                this.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Error: " + ex.getLocalizedMessage());
                this.setStop(true);
                System.exit(1);
            }
        }
    }
}