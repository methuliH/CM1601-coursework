package utils;
import models.Dealer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DealerManager {
    private List<Dealer> dealers;
    private static final int DEALERS_TO_SELECT = 4;

    public DealerManager(List<Dealer> dealers) {
        this.dealers = dealers; //?
    }

    //Select random dealers
    public List<Dealer> selectRandomDealer() {
        if (this.dealers.size() < DEALERS_TO_SELECT) {
            System.out.println("Not enough dealers");
            return new ArrayList<>(); //?
        }

        //Creating a copy of teh dealer list
        List<Dealer> dealersCopy = new ArrayList<>(this.dealers);
        List<Dealer> selectedDealers = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < DEALERS_TO_SELECT; i++) {
            int randomIndex = rand.nextInt(dealersCopy.size());

            Dealer randomDealer = dealersCopy.get(randomIndex);

            selectedDealers.add(randomDealer);
            dealersCopy.remove(randomIndex);
        }
        return sortByLocation(selectedDealers);
    }

    private List<Dealer> sortByLocation(List<Dealer> selectedDealers) {
        int n = selectedDealers.size();

        //Sorting by location using bubble sort
        for (int pass = 0; pass < n - 1; pass++) {
            for (int i = 0; i < n - 1 - pass; i++) {
                String location1 = selectedDealers.get(i).getLocation().toLowerCase();
                String location2 = selectedDealers.get(i + 1).getLocation().toLowerCase();

                // If first location > second location alphabetically, swap
                if (location1.compareTo(location2) > 0) {
                    Dealer temp = selectedDealers.get(i);
                    selectedDealers.set(i, selectedDealers.get(i + 1));
                    selectedDealers.set(i + 1, temp);
                }
            }
        }

        return selectedDealers;
    }

    public List<Dealer> getAllDealers(){
        return this.dealers;
    }
}
