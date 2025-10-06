package Pz5;

    class SportTeam {
        private String teamName;
        private int gamesWon;
        private int gamesLost;

        public SportTeam(String teamName, int gamesWon, int gamesLost) {
            this.teamName = teamName;
            this.gamesWon = gamesWon;
            this.gamesLost = gamesLost;
        }

        public String getTeamName() {
            return teamName;
        }

        public int getGamesWon() {
            return gamesWon;
        }

        public int getGamesLost() {
            return gamesLost;
        }

        public double getWinPercentage() {
            int totalGames = gamesWon + gamesLost;
            if (totalGames == 0) return 0.0;
            return (double) gamesWon / totalGames * 100;
        }

        @Override
        public String toString() {
            return String.format("%s - Побед: %d, Поражений: %d, Процент побед: %.1f%%",
                    teamName, gamesWon, gamesLost, getWinPercentage());
        }
    }

    @FunctionalInterface
    interface Processor<T> {
        void process(T obj);
    }


