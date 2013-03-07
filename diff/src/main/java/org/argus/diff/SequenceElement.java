package org.argus.diff;

public class SequenceElement<T> {

	public enum Status {
		ADDED, REMOVED, UNTOUCHED;
	}

	protected T element;
	protected Status status;

	public SequenceElement(T e, Status s) {
		element = e;
		status = s;
	}

	public T getElement() {
		return element;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		String ret;
		switch (status) {
		case ADDED:
			ret = "+ ";
			break;
		case REMOVED:
			ret = "- ";
			break;
		default:
			ret = "  ";
			break;
		}
		ret = ret.concat(element.toString());

		return ret;
	}
}