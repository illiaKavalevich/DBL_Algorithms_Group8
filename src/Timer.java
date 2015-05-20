/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s131061
 */
public class Timer {
    long start;
    long end;
    
    public Timer(int seconds) {
        start = System.currentTimeMillis();
        end = start + seconds * 1000;
    }
    
}
