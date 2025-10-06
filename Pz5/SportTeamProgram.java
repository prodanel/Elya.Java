package Pz5;

public class SportTeamProgram {
    public static void main(String[] args) {
        SportTeam[] teams = {
                new SportTeam("Динамо", 25, 5),
                new SportTeam("Спартак", 18, 12),
                new SportTeam("ЦСКА", 30, 0),
                new SportTeam("Локомотив", 15, 15),
                new SportTeam("Зенит", 28, 2)
        };

        System.out.println("=== ВСЕ КОМАНДЫ ===");

        Processor<SportTeam> teamPrinter = (team) -> {
            System.out.println(team.toString());
        };

        for (SportTeam team : teams) {
            teamPrinter.process(team);
        }

        System.out.println("\n=== КОМАНДЫ С ПРОЦЕНТОМ ПОБЕД > 70% ===");

        Processor<SportTeam> highWinRatePrinter = (team) -> {
            if (team.getWinPercentage() > 70.0) {
                System.out.println(team.toString());
            }
        };

        for (SportTeam team : teams) {
            highWinRatePrinter.process(team);
        }

        System.out.println("\n=== ОТСОРТИРОВАННЫЕ КОМАНДЫ ПО ПРОЦЕНТУ ПОБЕД ===");

        for (int i = 0; i < teams.length - 1; i++) {
            for (int j = 0; j < teams.length - i - 1; j++) {
                if (teams[j].getWinPercentage() < teams[j + 1].getWinPercentage()) {
                    SportTeam temp = teams[j];
                    teams[j] = teams[j + 1];
                    teams[j + 1] = temp;
                }
            }
        }

        Processor<SportTeam> sortedPrinter = (team) -> {
            System.out.printf("%s - %.1f%% побед\n",
                    team.getTeamName(), team.getWinPercentage());
        };

        for (SportTeam team : teams) {
            sortedPrinter.process(team);
        }

        System.out.println("\n=== ДОПОЛНИТЕЛЬНАЯ СТАТИСТИКА ===");

        Processor<SportTeam> gameStats = (team) -> {int totalGames = team.getGamesWon() + team.getGamesLost();
            if (totalGames > 30) {
                System.out.println(team.getTeamName() + " - сыграла " + totalGames + " игр");
            }
        };

        for (SportTeam team : teams) {
            gameStats.process(team);
        }
    }
}


