package Laboratory_work_6;

class NumberExpr extends Expr {
    double value;

    NumberExpr(double value) {
        this.value = value;
    }

    @Override
    double evaluate() {
        return value;
    }
}