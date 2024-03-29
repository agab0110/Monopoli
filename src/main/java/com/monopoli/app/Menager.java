package com.monopoli.app;

import java.io.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Class for managing contracts and players
 * This class saves its state in menager.sr 
 * with the state of the players and the state of the contracts present
 * 
 * @author Alessandro Gabriele
*/
public class Menager implements Serializable{
    private List<Player> players;
    private List<Contract> contracts;
    private List<Card> suddenCards;
    private List<Card> chanceCards;
    private int numContract;
    public static final long serialVersionUID = 1L;

    public Menager() {
        
    }

    public void constructor(){
        this.players = new ArrayList<>();
        this.contracts = new ArrayList<>();
        this.suddenCards = new ArrayList<>();
        this.chanceCards = new ArrayList<>();
    }

    /**
     * Method for loading the previously saved game in menager.sr
     * throw an IOExceptoion and handle FileNotFoundException and ClassNotFoundException
     * in case the file or class Manager is not found
     * 
     * @return Manager if found, null otherwise
     * @throws IOException if the file or class is not found
     */
    public static Menager loadMenager() throws IOException {
        try (
                FileInputStream fileInputStream = new FileInputStream("menager.sr");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
        ) {
            Object o = objectInputStream.readObject();
            return (Menager) o;
        } catch (FileNotFoundException e) {
            return null;
        } catch (ClassNotFoundException ignore) {
            return null;
        }
    }

    /**
     * Method for saving the game in manager.sr
     * throws an IOException in case of error writing to manager.sr
     * 
     * @throws IOException in case of a writing error
     */
    public void saveMenager() throws IOException {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream("menager.sr");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(this);
        }
    }

    /**
     * Method to search if there is already a game to load
     * throws an IOException and handles FileNotFoundException in case the menager.sr is not found
     * 
     * @return true if the game is found, false otherwise
     * @throws IOException and handles FileNotFoundException in case the menager.sr is not found
     */
    public static boolean searchMenager() throws IOException {
        try (
                FileInputStream fileInputStream = new FileInputStream("menager.sr");
        ) {
            return true;      
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public void start() {
        switch (players.size()) {
            case 2:
                for (Player player : players) {
                    player.setMoney(8750);
                }
                numContract = 7;
                break;
            case 3:
                for (Player player : players) {
                    player.setMoney(7500);
                }
                numContract = 6;
                break;
            case 4:
                for (Player player : players) {
                    player.setMoney(6250);
                }
                numContract = 5;
                break;
            case 5:
                for (Player player : players) {
                    player.setMoney(5000);
                }
                numContract = 4;
                break;
            case 6:
                for (Player player : players) {
                    player.setMoney(3750);
                }
                numContract = 3;
                break;
        }

        this.inizializeContracts();
        this.inizializeCards();
        this.assignContracts(numContract);
    }

    private void inizializeContracts() {
        Contract vicoloCorto = new Contract("Vicolo corto", 60, 2);
        contracts.add(vicoloCorto);
        Contract vicoloStretto = new Contract("Vicolo Stretto", 60, 4);
        contracts.add(vicoloStretto);
        Contract bastioniGranSasso = new Contract("Bastoni Gran Sasso", 100, 6);
        contracts.add(bastioniGranSasso);
        Contract vialeMonterosa = new Contract("Viale Monterosa", 100, 6);
        contracts.add(vialeMonterosa);
        Contract vialeVesuvio = new Contract("Viale Vesuvio",120, 8);
        contracts.add(vialeVesuvio);
        Contract viaAccademia = new Contract("Via Accademia", 140, 10);
        contracts.add(viaAccademia);
        Contract corsoAteneo = new Contract("Corso Ateneo",140,10);
        contracts.add(corsoAteneo);
        Contract piazzaUniversita = new Contract("Piazza Università", 160, 12);
        contracts.add(piazzaUniversita);
        Contract viaVerdi = new Contract("Via Verdi", 180, 14);
        contracts.add(viaVerdi);
        Contract corsoRaffaello = new Contract("Corso Raffaello",180,14);
        contracts.add(corsoRaffaello);
        Contract piazzaDante = new Contract("Piazza Dante", 200, 16);
        contracts.add(piazzaDante);
        Contract viaMarcoPolo = new Contract("Via Marco Polo", 220, 18);
        contracts.add(viaMarcoPolo);
        Contract corsoMagellano = new Contract("Corso Magellano", 220, 18);
        contracts.add(corsoMagellano);
        Contract largoColombo =  new Contract("Largo Colombo", 240, 20);
        contracts.add(largoColombo);
        Contract vialeCostantino = new Contract("Viale Costantino", 260, 22);
        contracts.add(vialeCostantino);
        Contract vialeTraiano = new Contract("Viale Traiano", 260, 22);
        contracts.add(vialeTraiano);
        Contract piazzaGiulioCesare = new Contract("Piazza Giulio Cesare", 280, 24);
        contracts.add(piazzaGiulioCesare);
        Contract viaRoma = new Contract("Via Roma", 300, 26);
        contracts.add(viaRoma);
        Contract corsoImpero = new Contract("Corso Impero", 300, 26);
        contracts.add(corsoImpero);
        Contract largoAugusto = new Contract("Largo Augusto", 320, 28);
        contracts.add(largoAugusto);
        Contract vialeDeiGiardini = new Contract("Viale dei Giardini", 350, 35);
        contracts.add(vialeDeiGiardini);
        Contract parcoDellaVittoria = new Contract("Parco della Vittoria", 400, 40);
        contracts.add(parcoDellaVittoria);
        Contract stazioneSud = new Contract("Stazione Sud", 200, 25);
        contracts.add(stazioneSud);
        Contract stazioneNord = new Contract("Stazione Nord", 200, 25);
        contracts.add(stazioneNord);
        Contract stazioneEst = new Contract("Stazione Est", 200, 25);
        contracts.add(stazioneEst);
        Contract stazioneOvest = new Contract("Stazione Ovest", 200, 25);
        contracts.add(stazioneOvest); 
    }

    private void inizializeCards() {
        Card suddenCard1 = new Card("Imprevisto", "Andate fino al largo colombo: se passate dal via ritirate 200 euro", 1);
        suddenCards.add(suddenCard1);
        Card suddenCard2 = new Card("Imprevisto", "Andate in prigione direttamente e senza passare dal via", 2);
        suddenCards.add(suddenCard2);
        Card suddenCard3 = new Card("Imprevisto", "Fate 3 passi indietro", 3);
        suddenCards.add(suddenCard3);
        Card suddenCard4 = new Card("Imprevisto", "Andate fino a via accademia: se passate dal via ritirate 200 euro", 4);
        suddenCards.add(suddenCard4);
        Card suddenCard5 = new Card("Imprevisto", "Versate 50 euro per beneficienza", 5);
        suddenCards.add(suddenCard5);
        Card suddenCard6 = new Card("Imprevisto", "Andate alla stazione nord: se passate dal via ritirate 200 euro", 6);
        suddenCards.add(suddenCard6);
        Card suddenCard7 = new Card("Imprevisto", "Multa di 40 euro per aver guidato senza patente", 7);
        suddenCards.add(suddenCard7);
        Card suddenCard8 = new Card("Imprevisto", "Andate fino al parco della vittoria", 8);
        suddenCards.add(suddenCard8);
        Card suddenCard9 = new Card("Imprevisto", "Matrimonio in famiglia: spese impreviste 375 euro", 9);
        suddenCards.add(suddenCard9);
        Card suddenCard10 = new Card("Imprevisto", "Uscite gratis di prigione", 10);
        suddenCards.add(suddenCard10);
        Card suddenCard11 = new Card("Imprevisto", "Maturano le cedole delle vostre cartelle di rendita, ritirate 375 euro", 11);
        suddenCards.add(suddenCard11);
        Card suddenCard12 = new Card("Imprevisto", "La banca vi paga gli interessi del vostro conto corrente, ritirate 125 euro", 12);
        suddenCards.add(suddenCard12);
        Card suddenCard13 = new Card("Imprevisto", "Andate avanti fino al via", 13);
        suddenCards.add(suddenCard13);
        Card suddenCard14 = new Card("Imprevisto", "Avete vinto un terno al lotto: ritirate 250 euro", 14);
        suddenCards.add(suddenCard14);
        Card suddenCard15 = new Card("Imprevisto", "Andate in prigione direttamente e senza passare dal via", 15);
        suddenCards.add(suddenCard15);

        Card chanceCard1 = new Card("Probabilita'", "Ritornate al vicolo corto", 1);
        chanceCards.add(chanceCard1);
        Card chanceCard2 = new Card("Probabilita'", "È maturata la cedola delle vostre azioni: ritirate 60 euro", 2);
        chanceCards.add(chanceCard2);
        Card chanceCard3 = new Card("Probabilita'", "Rimborso tassa sul reddito: ritirate 50 euro dalla banca", 3);
        chanceCards.add(chanceCard3);
        Card chanceCard4 = new Card("Probabilita'", "Avete vinto il secondo premio in un concorso di bellezza: ritirate 25 euro", 4);
        chanceCards.add(chanceCard4);
        Card chanceCard5 = new Card("Probabilita'", "Scade il vostro premio di assicurazione: pagate 125 euro", 5);
        chanceCards.add(chanceCard5);
        Card chanceCard6 = new Card("Probabilita'", "Uscite gratis di prigione", 6);
        chanceCards.add(chanceCard6);
        Card chanceCard7 = new Card("Probabilita'", "Avete vinto un premio di consolazione alla lotteria di Merano: ritirate 250 euro", 7);
        chanceCards.add(chanceCard7);
        Card chanceCard8 = new Card("Probabilita'", "Andate fino al via", 8);
        chanceCards.add(chanceCard8);
        Card chanceCard9 = new Card("Probabilita'", "Pagate una multa di 25 euro, oppure prendete un cartoncino dagli imprevisti", 9);
        chanceCards.add(chanceCard9);
        Card chanceCard10 = new Card("Probabilita'", "Siete creditori verso la banca di 500 euro, ritirateli", 10);
        chanceCards.add(chanceCard10);
        Card chanceCard11 = new Card("Probabilita'", "Avete perso una causa: pagate 250 euro", 11);
        chanceCards.add(chanceCard11);
        Card chanceCard12 = new Card("Probabilita'", "Andate in prigione direttamente e senza passare dal via", 12);
        chanceCards.add(chanceCard12);
        Card chanceCard13 = new Card("Probabilita'", "È il vostro compleanno: ogni giocatore vi regala 25 euro", 13);
        chanceCards.add(chanceCard13);
        Card chanceCard14 = new Card("Probabilita'", "Avete ceduto delle azioni: ricavate 125 euro", 14);
        chanceCards.add(chanceCard14);
        Card chanceCard15 = new Card("Probabilita'", "Pagate il conto del dottore: 125 euro", 15);
        chanceCards.add(chanceCard15);

        Collections.shuffle(chanceCards);
        Collections.shuffle(suddenCards);
    }
    
    /**
     * Method for randomly assigning contracts to all players
     * 
     * @param numContract numbers of contracts to be assigned to each player
     */
    private void assignContracts(int numContract) {
        Collections.shuffle(contracts);

        int c = 0;
        for (Player player : players) {
            for (int i = 0; i < numContract; i++) { 
                    player.addContract(contracts.get(c));
                    c++;
                }
            }
    }

    /**
     * Method for paying rent
     * throw MoneyException if player.getMoney() < contract.getRent()
     * 
     * @param player the player who pays the rent
     * @param contract the contract from which the lease is taken
     * @throws MoneyException if player.getMoney() < contract.getRent()
     */
    public void payRent(Player player, Contract contract) throws MoneyException{
        player.subMoney(contract.getRent());
        contract.getOwner().addMoney(contract.getRent());
    }

    /**
     * Method of buying a contract,
     * launch a MoneyExeption if player.getMoney() < contract.getPrice()
     * 
     * @param player the buying player
     * @param contract the contract to buy
     * @throws MoneyException if the money is not enough
     */
    public void buyContract(Player player, Contract contract) throws MoneyException{
        player.subMoney(contract.getPrice());
        contract.setOwner(player);
        player.addContract(contract);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public List<Contract> getContracts(){
        return contracts;
    }

    public List<Card> getSuddenCards(){
        return this.suddenCards;
    }

    public List<Card> getChanceCards(){
        return this.chanceCards;
    }

    /**
     * Method for adding a player into the player list
     * throws PlayerException if name is empty, duplicate or color is duplicate
     * 
     * @param player the player to add
     * @throws PlayerException if the name is empty or duplicated, or if the color is duplicated
     */
    public void addPlayer(Player player) throws PlayerException{
        for (Player p : players) {
            if (player.getName() == null) {
                throw new PlayerException("Il nome non può essere vuoto");
            }
            if (p.getName().equals(player.getName())) {
                throw new PlayerException("Nome duplicato");
            }
            if (p.getColor().equals(player.getColor())) {
                throw new PlayerException("Colore duplicato");
            }
        }

        this.players.add(player);
    }

    public int getPlayersSize() {
        return players.size();
    }
}
