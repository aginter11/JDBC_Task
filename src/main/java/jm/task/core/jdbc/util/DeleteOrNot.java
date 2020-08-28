package jm.task.core.jdbc.util;

import java.util.Scanner;

public class DeleteOrNot {

    public static boolean delete() {
        System.out.println("Вы точно хотите удалить всех пользователей? да/нет");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();

        if (s.equals("да")) return true;
        else return false;
    }
}
