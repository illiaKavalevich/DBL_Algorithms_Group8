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
    Runnable run;
    public Timer(int seconds, Algorithm a) {
        start = System.currentTimeMillis();
        end = start + seconds * 1000;
        run = new Runnable() {
            @Override
            public void run() {
                while(System.currentTimeMillis() < end){
                    //wait
                }
                a.stopRunning();
            }
        };
    }
    public void start(){
        Thread timerThread = new Thread(run,"timerThread");
        timerThread.start();
    }
}
