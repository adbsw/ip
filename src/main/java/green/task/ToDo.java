package green.task;

public class ToDo extends Task {


    public ToDo(String description) {
        super(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof ToDo)) {
            return false;
        }

        return super.equals(o);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

}
