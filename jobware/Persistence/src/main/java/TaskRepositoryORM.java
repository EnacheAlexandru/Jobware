import domain.Task;
import domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskRepositoryORM implements TaskRepositoryInterface {
    private final SessionFactory sessionFactory;

    public TaskRepositoryORM(SessionFactoryORM sessionFactory) {
        this.sessionFactory = sessionFactory.getSessionFactory();
    }

    @Override
    public Task saveTask(User user, String description, String importance, String status, String whenDate) {
        Transaction tx = null;
        Task task = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            task = new Task(user, description, importance, status, whenDate);
            int taskID = (int) session.save(task);
            task.setId(taskID);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public List<Task> getTasksByUsername(String username) {
        Transaction tx = null;
        List<Task> tasks = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            tasks = session.createQuery("FROM Task WHERE username = :username AND (whenDate = :whendate OR (whenDate != :whendate AND status = 'Unfinished'))", Task.class).setParameter("whendate", today).setParameter("username", username).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public List<Task> getTasks() {
        Transaction tx = null;
        List<Task> tasks = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            tasks = session.createQuery("FROM Task WHERE whenDate = :whendate OR (whenDate != :whendate AND status = 'Unfinished')", Task.class).setParameter("whendate", today).list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public Task setStatus(int taskID, boolean status) {
        Transaction tx = null;
        Task task = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            task = session.get(Task.class, taskID);
            if (status) {
                task.setStatus("Finished");
            }
            else {
                task.setStatus("Unfinished");
            }
            session.update(task);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return task;
    }
}
