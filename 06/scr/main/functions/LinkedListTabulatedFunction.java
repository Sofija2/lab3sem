package functions;

import java.io.*;  // Для работы с файлами

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {

    private static class FunctionNode {

        public FunctionPoint data;
        public FunctionNode prev;
        public FunctionNode next;

        public FunctionNode(FunctionPoint data) {
            this.data = data;
            prev = null;
            next = null;
        }
    }

    private FunctionNode head;
    private int countPoints;

    public LinkedListTabulatedFunction(FunctionPoint[] points) throws IllegalArgumentException {
        if (points.length < 2) {
            throw new IllegalArgumentException("Must have at least two points.");
        }
        // Проверка упорядоченности точек по абсциссе
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Points must be sorted by x coordinate.");
            }
        }
        // Инициализация списка
        head = new FunctionNode(new FunctionPoint(Double.NaN, Double.NaN)); // Дублирующая точка
        head.next = new FunctionNode(points[0]);
        FunctionNode current = head.next;
        countPoints = 1;
        for (int i = 1; i < points.length; i++) {
            FunctionNode newNode = new FunctionNode(points[i]);
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
            countPoints++;
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int numOfPoints) throws IllegalArgumentException {
        if (leftX >= rightX || numOfPoints < 2) {
            throw new IllegalArgumentException("Invalid input parameters.");
        }

        countPoints = numOfPoints;
        double step = (rightX - leftX) / (numOfPoints - 1); // Calculate step correctly

        head = new FunctionNode(new FunctionPoint(leftX, 0)); // Initialize head node
        FunctionNode current = head;

        for (int i = 1; i < numOfPoints; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = new FunctionNode(new FunctionPoint(x, 0));
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Invalid input parameters.");
        }

        countPoints = values.length;
        double step = (rightX - leftX) / (values.length - 1); // Calculate step correctly

        head = new FunctionNode(new FunctionPoint(leftX, values[0])); // Initialize head node
        FunctionNode current = head;

        for (int i = 1; i < values.length; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = new FunctionNode(new FunctionPoint(x, values[i]));
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        // Начинаем с head, чтобы включить крайнее левое значение
        FunctionNode current = head; // Изменено на head вместо head.next

        while (current != null) {
            sb.append(current.data.toString()); // Вызов toString() для data
            current = current.next;
            if (current != null) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof TabulatedFunction)) {
            return false;
        }
        TabulatedFunction other = (TabulatedFunction) obj;
        if (this.countPoints != other.getPointsCount()) {
            return false;
        }
        if (obj instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction o = (LinkedListTabulatedFunction) obj;
            FunctionNode current1 = this.head.next;
            FunctionNode current2 = o.head.next;
            while (current1 != null) {
                if (!current1.data.equals(current2.data)) {
                    return false;
                }
                current1 = current1.next;
                current2 = current2.next;
            }
            return true;
        }
        for (int i = 0; i < countPoints; i++) {
            if (!this.getPoint(i).equals(other.getPoint(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        for (int i = 0; i < this.getPointsCount(); i++) {
            hash += 52 * i * this.getPoint(i).hashCode();
        }
        hash += 52 * this.getPointsCount();
        return hash;
    }

    public Object clone() throws CloneNotSupportedException {
        LinkedListTabulatedFunction clone = (LinkedListTabulatedFunction) super.clone();
        clone.head = null;
        FunctionNode current = head;
        FunctionNode previousCloneNode = null;
        while (current != null) {
            // Создаем новый узел с клонированными данными
            FunctionNode newNode = new FunctionNode((FunctionPoint) current.data.clone());
            if (clone.head == null) {
                // Если это первый узел, устанавливаем его как голову
                clone.head = newNode;
            } else {
                // Связываем новый узел с предыдущим
                previousCloneNode.next = newNode;
                newNode.prev = previousCloneNode;
            }
            // Обновляем предыдущий узел
            previousCloneNode = newNode;
            // Переходим к следующему узлу оригинального списка
            current = current.next;
        }
        // Копируем количество точек
        clone.countPoints = this.countPoints;
        return clone;
    }

    public FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Проверка на нулевой или первый индекс
        if (index == 0) {
            return head.next; // Возвращаем первый значимый элемент
        } else if (index == 1) {
            return head.next.next; // Возвращаем второй значимый элемент
        }

        // Используем двоичный поиск для поиска элемента по индексу
        int left = 0;
        int right = countPoints - 1;
        FunctionNode current = head;
        while (left <= right) {
            int mid = (left + right) / 2;

            // Перемещаемся к элементу, соответствующему среднему индексу
            current = head;
            for (int i = 0; i < mid; i++) {
                current = current.next;
            }

            // Сравнение индекса с текущим средним индексом
            if (mid == index) {
                return current;
            } else if (mid < index) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Элемент не найден - ошибка
        throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
    }

    // Добавляет новый элемент в конец списка и возвращает ссылку на объект этого элемента
    public FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint(getRightDomainBorder(), 0));

        if (head == null) { // Список пуст
            head = newNode;
        } else { // Список не пуст
            FunctionNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
            newNode.prev = current;
        }
        countPoints++;
        return newNode;
    }

    // Добавляет новый элемент в указанную позицию списка и возвращает ссылку на объект этого элемента
    public FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        FunctionNode newNode = new FunctionNode(new FunctionPoint(getPointX(index - 1) + (getPointX(index) - getPointX(index - 1)) / 2, 0));

        if (index == 0) { // Вставка в начало списка
            newNode.next = head.next;
            head.next.prev = newNode;
            head.next = newNode;
            newNode.prev = head;
        } else { // Вставка в другое место списка
            FunctionNode current = getNodeByIndex(index - 1); // Получение ссылки на предыдущий элемент
            newNode.next = current.next;
            newNode.prev = current;
            if (current.next != null) {
                current.next.prev = newNode;
            }
            current.next = newNode;
        }
        countPoints++;
        return newNode;
    }

    // Удаляет элемент списка по номеру и возвращает ссылку на объект удаленного элемента
    public FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        FunctionNode deletedNode;

        if (index == 0) { // Удаление первого элемента
            deletedNode = head.next;
            head.next = deletedNode.next;
            if (deletedNode.next != null) {
                deletedNode.next.prev = head;
            }
        } else { // Удаление элемента не из начала списка
            FunctionNode current = getNodeByIndex(index - 1);
            deletedNode = current.next;
            current.next = deletedNode.next;
            if (deletedNode.next != null) {
                deletedNode.next.prev = current;
            }
        }
        countPoints--;
        return deletedNode;
    }

    @Override
    public double getLeftDomainBorder() {
        if (head == null) {
            return Double.NaN;
        }
        return head.next.data.getX(); // Доступ к x-координате первого элемента списка
    }

    @Override
    public double getRightDomainBorder() {
        if (head == null) {
            return Double.NaN;
        }
        return head.next.prev.data.getX(); // Доступ к x-координате последнего элемента списка
    }

    @Override
    public double getFunctionValue(double x) {
        if (head == null) {
            return Double.NaN;
        }

        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        // Проверка на совпадение x с точками в списке
        FunctionNode current = head.next;
        while (current != null && current.data.getX() < x) {
            current = current.next;
        }

        if (current != null && current.data.getX() == x) {
            return current.data.getY();
        }

        // Линейная интерполяция
        if (current == null) {
            current = head.next.prev;
        }
        double x1 = current.prev.data.getX();
        double y1 = current.prev.data.getY();
        double x2 = current.data.getX();
        double y2 = current.data.getY();
        return ((y2 - y1) / (x2 - x1)) * (x - x1) + y1;
    }

    @Override
    public int getPointsCount() {
        return countPoints;
    }

    @Override
    public FunctionPoint getPoint(int index) {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод getNodeByIndex для доступа к элементу по индексу
        FunctionNode current = getNodeByIndex(index);
        return current.data;
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод getNodeByIndex для доступа к элементу по индексу
        FunctionNode current = getNodeByIndex(index);

        // Проверка корректности x-координаты точки
        if (index == 0 && current.next != null && point.getX() < current.next.data.getX()) {
            current.data = point;
        } else if (index == countPoints - 1 && current.prev != null && point.getX() > current.prev.data.getX()) {
            current.data = point;
        } else if (current.prev != null && current.next != null && current.prev.data.getX() <= point.getX() && point.getX() <= current.next.data.getX()) {
            current.data = point;
        } else {
            throw new InappropriateFunctionPointException("Incorrect x value for the point.");
        }
    }

    @Override
    public double getPointX(int index) {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод getNodeByIndex для доступа к элементу по индексу
        FunctionNode current = getNodeByIndex(index);
        return current.data.getX();
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод getNodeByIndex для доступа к элементу по индексу
        FunctionNode current = getNodeByIndex(index);

        // Проверка корректности x-координаты точки
        if (index == 0 && current.next != null && x < current.next.data.getX()) {
            current.data.setX(x);
        } else if (index == countPoints - 1 && current.prev != null && x > current.prev.data.getX()) {
            current.data.setX(x);
        } else if (current.prev != null && current.next != null && current.prev.data.getX() < x && x < current.next.data.getX()) {
            current.data.setX(x);
        } else {
            throw new InappropriateFunctionPointException("Incorrect x value for the point.");
        }
    }

    @Override
    public double getPointY(int index) {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод getNodeByIndex для доступа к элементу по индексу
        FunctionNode current = getNodeByIndex(index);
        return current.data.getY();
    }

    @Override
    public void setPointY(int index, double y) {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод getNodeByIndex для доступа к элементу по индексу
        FunctionNode current = getNodeByIndex(index);
        current.data.setY(y);
    }

    @Override
    public void deletePoint(int index) {
        if (head == null || index < 0 || index >= countPoints) {
            throw new FunctionPointIndexOutOfBoundsException("Invalid index.");
        }

        // Используем метод deleteNodeByIndex для удаления элемента по индексу
        deleteNodeByIndex(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (head == null) {
            head = new FunctionNode(point);
            countPoints++;
            return;
        }

        // Проверка корректности x-координаты точки
        if (getPointX(0) > point.getX() || getPointX(countPoints - 1) < point.getX()) {
            throw new InappropriateFunctionPointException("Incorrect x value for the point.");
        }

        // Проверка на дубликаты x-координат
        FunctionNode current = head.next;
        while (current != null && current.data.getX() < point.getX()) {
            current = current.next;
        }

        if (current != null && current.data.getX() == point.getX()) {
            throw new InappropriateFunctionPointException("Point with the same x-coordinate already exists.");
        }

        // Вставка новой точки в список
        int index = 0;
        current = head.next;
        while (current != null && current.data.getX() < point.getX()) {
            current = current.next;
            index++;
        }

        addNodeByIndex(index);
        setPoint(index, point);
    }
}
