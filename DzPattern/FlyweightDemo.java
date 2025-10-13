package DzPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class FlyweightDemo {

    static class TreeType {
        private final String name;
        private final String color;
        private final String texture;

        public TreeType(String name, String color, String texture) {
            this.name = name;
            this.color = color;
            this.texture = texture;
        }

        public void draw(int x, int y) {
            System.out.printf("Рисуем '%s' дерево (цвет: %s, текстура: %s) в точке (%d, %d)\n",
                    name, color, texture, x, y);
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }

        public String getTexture() {
            return texture;
        }
    }
    static class TreeFactory {
        private static final Map<String, TreeType> treeTypes = new HashMap<>();

        public static TreeType getTreeType(String name, String color, String texture) {
            String key = name + "_" + color + "_" + texture;
            TreeType treeType = treeTypes.get(key);

            if (treeType == null) {
                treeType = new TreeType(name, color, texture);
                treeTypes.put(key, treeType);
                System.out.println("Создан новый тип дерева: " + key);
            } else {
                System.out.println("Взят существующий тип дерева: " + key);
            }
            return treeType;
        }

        public static int getTreeTypesCount() {
            return treeTypes.size();
        }
    }
    static class Tree {
        private int x;
        private int y;
        private TreeType type;

        public Tree(int x, int y, TreeType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public void draw() {
            type.draw(x, y);
        }
    }
    static class Forest {
        private List<Tree> trees = new ArrayList<>();

        public void plantTree(int x, int y, String name, String color, String texture) {
            TreeType treeType = TreeFactory.getTreeType(name, color, texture);
            Tree tree = new Tree(x, y, treeType);
            trees.add(tree);
        }

        public void drawForest() {
            for (Tree tree : trees) {
                tree.draw();
            }
        }

        public int getTreeCount() {
            return trees.size();
        }
    }
    public static void main(String[] args) {
        Forest forest = new Forest();

        System.out.println("=== Сажаем деревья ===");
        forest.plantTree(10, 20, "Дуб", "Зеленый", "Крупная");
        forest.plantTree(50, 30, "Сосна", "Темно-зеленый", "Мелкая");
        forest.plantTree(15, 25, "Дуб", "Зеленый", "Крупная");
        forest.plantTree(70, 40, "Береза", "Белый", "Гладкая");
        forest.plantTree(25, 35, "Сосна", "Темно-зеленый", "Мелкая");
        forest.plantTree(80, 60, "Дуб", "Зеленый", "Крупная");
        forest.plantTree(90, 70, "Сосна", "Темно-зеленый", "Мелкая");

        System.out.println("\n=== Рисуем лес ===");
        forest.drawForest();

        System.out.println("\n=== Анализ использования памяти ===");
        System.out.println("Всего посажено деревьев: " + forest.getTreeCount());
        System.out.println("Создано уникальных типов деревьев: " + TreeFactory.getTreeTypesCount());
        System.out.println("Экономия: " + forest.getTreeCount() + " деревьев используют только " +
                TreeFactory.getTreeTypesCount() + " объектов TreeType");System.out.println("\n=== Дополнительная демонстрация ===");
        forest.plantTree(100, 100, "Дуб", "Коричневый", "Крупная");
        forest.plantTree(110, 110, "Клен", "Красный", "Резная");
        System.out.println("Теперь уникальных типов деревьев: " + TreeFactory.getTreeTypesCount());
    }
}
