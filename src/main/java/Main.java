//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import service.UserService;
import service.UserServiceImpl;
import model.User;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("DOCKER_HOST = " + System.getProperty("DOCKER_HOST"));
        UserService userService = new UserServiceImpl();
        Scanner scanner = new Scanner(System.in);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);

        while (true) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Найти по пользователя по ID");
            System.out.println("3. Вывести всех пользователей");
            System.out.println("4. Изменить пользователя");
            System.out.println("5. Удалить пользователя");
            System.out.println("0. Выход из программы");
            System.out.print("Ваш выбор: ");

            String input = scanner.nextLine();
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 0 до 5.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    try {
                        System.out.print("Введите имя: ");
                        String name = scanner.nextLine();
                        System.out.print("Введите email: ");
                        String email = scanner.nextLine();
                        System.out.print("Введите возраст: ");
                        int age = Integer.parseInt(scanner.nextLine());

                        if (userService.saveUser(new User(name, email, age))) {
                            System.out.println(">> Пользователь успешно создан.");
                        } else {
                            System.out.println(">> Ошибка при создании объекта в БД.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(">> Ошибка: Возраст должен быть целым числом.");
                    }
                }
                case 2 -> {
                    System.out.print("Введите ID пользователя для поиска: ");
                    try {
                        Long id = Long.parseLong(scanner.nextLine());
                        User u = userService.getUser(id);
                        System.out.println(u != null ? u : ">> Пользователь с ID " + id + " не найден.");
                    } catch (NumberFormatException e) {
                        System.out.println(">> Ошибка: некорректный формат ID.");
                    }
                }
                case 3 -> {
                    List<User> users = userService.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println(">> Список пользователей пуст.");
                    }
                    else {
                        users.forEach(u -> System.out.println(" • " + u));
                    }
                }
                case 4 -> {
                    System.out.print("Введите ID пользователя для изменения данных: ");
                    try {
                        Long id = Long.parseLong(scanner.nextLine());
                        User u = userService.getUser(id);
                        if (u != null) {
                            System.out.print("Новое имя [" + u.getName() + "]: ");
                            String newName = scanner.nextLine();
                            if (!newName.isEmpty()) {
                                u.setName(newName);
                            }
                            System.out.print("Новый возраст: ");
                            u.setAge(Integer.parseInt(scanner.nextLine()));
                            if (userService.updateUser(u)) {
                                System.out.println(">> Данные пользователя обновлены.");
                            }
                            else {
                                System.out.println(">> Не удалось сохранить изменения.");
                            }
                        } else {
                            System.out.println(">> Пользователь не найден.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(">> Ошибка: Введены неверные данные.");
                    }
                }
                case 5 -> {
                    System.out.print("Введите ID для удаления: ");
                    try {
                        Long id = Long.parseLong(scanner.nextLine());
                        if (userService.deleteUser(id)) {
                            System.out.println(">> Пользователь успешно удален.");
                        } else {
                            System.out.println(">> Ошибка: Пользователь с таким ID не существует.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(">> Ошибка: Некорректный ID.");
                    }
                }
                case 0 -> {
                    System.out.println("Выход...");
                    System.exit(0);
                }
                default -> System.out.println("Неверный пункт меню.");
            }
        }
    }
}