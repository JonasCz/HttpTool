package jonas.tool.httpRequestCreator.Util;

import java.util.List;
import java.util.ArrayList;

/**
* Represents a "Revision history" for the app's undo / redo feature.
* @author Jonas Czech (http://github.com/JonasCz/)
*/
public class UndoHistoryStack<E> {
	private List<E> list = new ArrayList<E>();
	private E current;
	private int currentIndex = 0;
	
	private OnChangeListener changeListener = new OnChangeListener() {
		@Override
		public void onChange() {
			//Do nothing by default
		}
	};

	public UndoHistoryStack (E first, OnChangeListener changeListener){
		current = first;
		list.add(first);
		
		if (changeListener != null) {
			this.changeListener = changeListener;
		}
	}
	
	public UndoHistoryStack<E> setCurrent (E e) {
		list = list.subList(0, ++currentIndex);
		list.add(e);
		current = e;
		changeListener.onChange();
		return this;
	}
	
	public E getCurrent () {
		return current;
	}
	
	public boolean canUndo () {
		return currentIndex > 0;
	}
	
	public UndoHistoryStack<E> undo () {
		if (canUndo()) {
			current = list.get(--currentIndex);
			changeListener.onChange();
		}
		return this;
	}
	
	public boolean canRedo () {
		return currentIndex < list.size() - 1;
	}

	public UndoHistoryStack<E> redo () {
		if (canRedo()) {
			current = list.get(++currentIndex);
			changeListener.onChange();
		}
		return this;
	}
	
	public interface OnChangeListener {
		void onChange ();
	}
}
