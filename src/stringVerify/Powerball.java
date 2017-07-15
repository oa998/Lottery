package stringVerify;

import java.util.*;

public class Powerball {

    static List<Integer> lotteryTicket;
    static Random r = new Random();
    static int[] powsOfTwo = new int[] {1, 2, 4, 8, 16, 32};
    static long original = 0, remaining = 0, iterations = 0;
    static Map<Integer, Long> noPBStats = new HashMap<>();
    static Map<Integer, Long> PBStats = new HashMap<>();
    static {
        for(int i = 0; i< 6; i++) {
            noPBStats.put(i, 0l);
            PBStats.put(i, 0l);
        }

    }


    public static void main(String... args){
        long start = new Date().getTime();
        lotteryTicket = pickSix();
        original = remaining = 1000000000l;
        int match;
        do{
            remaining -= 2; //buy a ticket
            iterations++;
            match = assessMatches(pickSix());
            int bc = Integer.bitCount(match);
            remaining += (match>31)? getPBPayout(bc-1): getNoPBPayout(bc);
            if(match<32)
                noPBStats.put(bc, noPBStats.get(bc)+1);
            else
                PBStats.put(bc-1, PBStats.get(bc-1)+1);
        }while(remaining > 2);
        long end = new Date().getTime();
        showStats();
        System.out.print("Time taken: ");
        System.out.println(showTime(end-start));
        System.out.println("Tickets bought "+iterations);
    }

    private static List<Integer> pickSix(){
        List<Integer> picks = new ArrayList<>(6);
        for(int i = 0; i < 5; i++) {
            int x;
            do{
                x = r.nextInt(69) + 1; //regular 5
            }while(picks.contains(x));
            picks.add(x);
        }
        picks.add(r.nextInt(26)+1); //powerball
        return picks;
    }

    //assumed that length of chosen and lottery ticket are exactly equal
    private static int assessMatches(List<Integer> chosen){
        int ret = 0;
        for(int i=0; i< lotteryTicket.size(); i++)
            if(lotteryTicket.get(i)==chosen.get(i))
                ret+=powsOfTwo[i];
        return ret;
    }

    private static int getPBPayout(int hits){
        //with powerball
        switch (hits) {
            case 1:
                return 4;
            case 2:
                return 7;
            case 3:
                return 100;
            case 4:
                return 50000;
            case 5:
                return 1500000000; //jackpot, 1.5 billion
            default:
                return 0;
        }
    }
    private static int getNoPBPayout(int hits) {
        //matching numbers without powerball
        switch (hits) {
            case 3:
                return 7;
            case 4:
                return 100;
            case 5:
                return 1000000;
            default:
                return 0;
        }
    }

    private static void showStats(){
        String pbFormat   = "%d matching including powerball: %-9d  Payout: $%11d";
        String nopbFormat = "%d matching  without  powerball: %-9d  Payout: $%11d";
        for(int i = 0; i < 6; i++){
            long noPBHits = noPBStats.get(i); //num matching balls
            long PBHits = PBStats.get(i);
            if(i>2)
                System.out.println(String.format(nopbFormat, i, noPBHits, (getNoPBPayout(i) * noPBHits)));
            else if(i==0)
                System.out.println(String.format("Zero balls matching the ticket: %-9d", noPBHits));
            System.out.println(String.format(pbFormat, i, PBHits, (getPBPayout(i) * PBHits)));
        }
    }

    private static String showTime(long millis){
        double time = millis * 1.0;
        if(millis>1000)
            time = (double) millis / 1000; //seconds
        else
            return (time + " miliseconds");
        if(time > 60)
            time /= 60;  //minutes
        else
            return (time + " seconds");
        if(time > 60) {
            time /= 60;  //hours
            return (time + " hours");
        }else
            return (time + " minutes");
    }


}
