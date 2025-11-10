package CharacterCounter;

class MutableBoolean {
    private boolean value;

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public MutableBoolean() {
        this(false);
    }

    public void setValue(boolean value){
        this.value = value;
    }

    public boolean isTrue(){
        return value;
    }

    public int toInt(){
        return value ? 1 : 0;
    }

}
