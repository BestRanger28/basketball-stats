import java.util.ArrayList;

public class GameData {
    public String date;

    public String awayTeam;
    public String homeTeam;

    public int awayPoints;
    public int homePoints;

    public ArrayList<PlayerStats> awayPlayers;
    public ArrayList<PlayerStats> homePlayers;

    public GameData() {
        awayPlayers = new ArrayList<>();
        homePlayers = new ArrayList<>();
    }
    public double[] data;
    public double[] homedata;
    public double[] awaydata;
    public double[] GetData(){
        data = new double[400];
        int i = 0;
        for (PlayerStats p : homePlayers) {
            data[0+i*20] = p.minutes/(double)p.games;
            data[1+i*20] = p.per;
            data[2+i*20] = p.trueShooting;
            data[3+i*20] = p.threePointAttemptRate;
            data[4+i*20] = p.freeThrowAttemptRate;
            data[5+i*20] = p.offensiveReboundPercentage/100.0;
            data[6+i*20] = p.defensiveReboundPercentage/100.0;
            data[7+i*20] = p.assistPercentage/100.0;
            data[8+i*20] = p.stealPercentage/100.0;
            data[9+i*20] = p.blockPercentage/100.0;
            data[10+i*20] = p.turnoverPercentage/100.0;
            data[11+i*20] = p.offensiveWinShares/12.0;
            data[12+i*20] = p.defensiveWinShares/6.0;
            data[13+i*20] = p.usage/100.0;
            data[14+i*20] = p.winShares48;
            data[15+i*20] = p.bpm/18.0;
            data[16+i*20] = p.offensiveBpm/12.0;
            data[17+i*20] = p.defensiveBpm/6.0;
            data[18+i*20] = p.vorp/10.0;
            data[19+i*20] = p.age/50.0;
            i++;
        }
        for (PlayerStats p : awayPlayers) {
            data[0+i*20] = p.minutes/(double)p.games;
            data[1+i*20] = p.per;
            data[2+i*20] = p.trueShooting;
            data[3+i*20] = p.threePointAttemptRate;
            data[4+i*20] = p.freeThrowAttemptRate;
            data[5+i*20] = p.offensiveReboundPercentage/100.0;
            data[6+i*20] = p.defensiveReboundPercentage/100.0;
            data[7+i*20] = p.assistPercentage/100.0;
            data[8+i*20] = p.stealPercentage/100.0;
            data[9+i*20] = p.blockPercentage/100.0;
            data[10+i*20] = p.turnoverPercentage/100.0;
            data[11+i*20] = p.offensiveWinShares/12.0;
            data[12+i*20] = p.defensiveWinShares/6.0;
            data[13+i*20] = p.usage/100.0;
            data[14+i*20] = p.winShares48;
            data[15+i*20] = p.bpm/18.0;
            data[16+i*20] = p.offensiveBpm/12.0;
            data[17+i*20] = p.defensiveBpm/6.0;
            data[18+i*20] = p.vorp/10.0;
            data[19+i*20] = p.age/50.0;
            i++;
        }
        return data;
    }
    public double[] GetHomeData(){
         homedata = new double[401];
        int i = 0;
        for (PlayerStats p : homePlayers) {
            homedata[0+i*20] = p.minutes/(double)p.games;
            homedata[1+i*20] = p.per;
            homedata[2+i*20] = p.trueShooting;
            homedata[3+i*20] = p.threePointAttemptRate;
            homedata[4+i*20] = p.freeThrowAttemptRate;
            homedata[5+i*20] = p.offensiveReboundPercentage/100.0;
            homedata[6+i*20] = p.defensiveReboundPercentage/100.0;
            homedata[7+i*20] = p.assistPercentage/100.0;
            homedata[8+i*20] = p.stealPercentage/100.0;
            homedata[9+i*20] = p.blockPercentage/100.0;
            homedata[10+i*20] = p.turnoverPercentage/100.0;
            homedata[11+i*20] = p.offensiveWinShares/12.0;
            homedata[12+i*20] = p.defensiveWinShares/6.0;
            homedata[13+i*20] = p.usage/100.0;
            homedata[14+i*20] = p.winShares48;
            homedata[15+i*20] = p.bpm/18.0;
            homedata[16+i*20] = p.offensiveBpm/12.0;
            homedata[17+i*20] = p.defensiveBpm/6.0;
            homedata[18+i*20] = p.vorp/10.0;
            homedata[19+i*20] = p.age/50.0;
            i++;
        }
        for (PlayerStats p : awayPlayers) {
            homedata[0+i*20] = p.minutes/(double)p.games;
            homedata[1+i*20] = p.per;
            homedata[2+i*20] = p.trueShooting;
            homedata[3+i*20] = p.threePointAttemptRate;
            homedata[4+i*20] = p.freeThrowAttemptRate;
            homedata[5+i*20] = p.offensiveReboundPercentage/100.0;
            homedata[6+i*20] = p.defensiveReboundPercentage/100.0;
            homedata[7+i*20] = p.assistPercentage/100.0;
            homedata[8+i*20] = p.stealPercentage/100.0;
            homedata[9+i*20] = p.blockPercentage/100.0;
            homedata[10+i*20] = p.turnoverPercentage/100.0;
            homedata[11+i*20] = p.offensiveWinShares/12.0;
            homedata[12+i*20] = p.defensiveWinShares/6.0;
            homedata[13+i*20] = p.usage/100.0;
            homedata[14+i*20] = p.winShares48;
            homedata[15+i*20] = p.bpm/18.0;
            homedata[16+i*20] = p.offensiveBpm/12.0;
            homedata[17+i*20] = p.defensiveBpm/6.0;
            homedata[18+i*20] = p.vorp/10.0;
            homedata[19+i*20] = p.age/50.0;
            i++;
        }
        homedata[400] = 1.0;
        return homedata;
    }
    public double[] GetAwayData(){
        awaydata = new double[401];
        int i = 0;
        for (PlayerStats p : awayPlayers) {
            awaydata[0+i*20] = p.minutes/(double)p.games;
            awaydata[1+i*20] = p.per;
            awaydata[2+i*20] = p.trueShooting;
            awaydata[3+i*20] = p.threePointAttemptRate;
            awaydata[4+i*20] = p.freeThrowAttemptRate;
            awaydata[5+i*20] = p.offensiveReboundPercentage/100.0;
            awaydata[6+i*20] = p.defensiveReboundPercentage/100.0;
            awaydata[7+i*20] = p.assistPercentage/100.0;
            awaydata[8+i*20] = p.stealPercentage/100.0;
            awaydata[9+i*20] = p.blockPercentage/100.0;
            awaydata[10+i*20] = p.turnoverPercentage/100.0;
            awaydata[11+i*20] = p.offensiveWinShares/12.0;
            awaydata[12+i*20] = p.defensiveWinShares/6.0;
            awaydata[13+i*20] = p.usage/100.0;
            awaydata[14+i*20] = p.winShares48;
            awaydata[15+i*20] = p.bpm/18.0;
            awaydata[16+i*20] = p.offensiveBpm/12.0;
            awaydata[17+i*20] = p.defensiveBpm/6.0;
            awaydata[18+i*20] = p.vorp/10.0;
            awaydata[19+i*20] = p.age/50.0;
            i++;
        }
        for (PlayerStats p : homePlayers) {
            awaydata[0+i*20] = p.minutes/(double)p.games;
            awaydata[1+i*20] = p.per;
            awaydata[2+i*20] = p.trueShooting;
            awaydata[3+i*20] = p.threePointAttemptRate;
            awaydata[4+i*20] = p.freeThrowAttemptRate;
            awaydata[5+i*20] = p.offensiveReboundPercentage/100.0;
            awaydata[6+i*20] = p.defensiveReboundPercentage/100.0;
            awaydata[7+i*20] = p.assistPercentage/100.0;
            awaydata[8+i*20] = p.stealPercentage/100.0;
            awaydata[9+i*20] = p.blockPercentage/100.0;
            awaydata[10+i*20] = p.turnoverPercentage/100.0;
            awaydata[11+i*20] = p.offensiveWinShares/12.0;
            awaydata[12+i*20] = p.defensiveWinShares/6.0;
            awaydata[13+i*20] = p.usage/100.0;
            awaydata[14+i*20] = p.winShares48;
            awaydata[15+i*20] = p.bpm/18.0;
            awaydata[16+i*20] = p.offensiveBpm/12.0;
            awaydata[17+i*20] = p.defensiveBpm/6.0;
            awaydata[18+i*20] = p.vorp/10.0;
            awaydata[19+i*20] = p.age/50.0;
            i++;
        }
        awaydata[400] = 0.0;
        return awaydata;
    }
}
