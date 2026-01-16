"use client"

import * as React from "react"

type ToastProps = {
  id: string
  title?: React.ReactNode
  description?: React.ReactNode
  action?: React.ReactNode
  duration?: number
  variant?: "default" | "destructive"
}

type ToastState = {
  toasts: ToastProps[]
}

type ToastAction =
  | { type: "ADD_TOAST"; toast: ToastProps }
  | { type: "UPDATE_TOAST"; toast: Partial<ToastProps> & { id: string } }
  | { type: "DISMISS_TOAST"; toastId?: string }
  | { type: "REMOVE_TOAST"; toastId?: string }

const TOAST_LIMIT = 1
const TOAST_REMOVE_DELAY = 1000

let count = 0
function genId() {
  count = (count + 1) % Number.MAX_SAFE_INTEGER
  return count.toString()
}

function reducer(state: ToastState, action: ToastAction): ToastState {
  switch (action.type) {
    case "ADD_TOAST": {
      const toasts = [action.toast, ...state.toasts].slice(0, TOAST_LIMIT)
      return { ...state, toasts }
    }
    case "UPDATE_TOAST": {
      const toasts = state.toasts.map((t) =>
        t.id === action.toast.id ? { ...t, ...action.toast } : t
      )
      return { ...state, toasts }
    }
    case "DISMISS_TOAST": {
      const toastId = action.toastId
      const toasts = state.toasts.map((t) =>
        toastId == null || t.id === toastId ? { ...t, duration: 0 } : t
      )
      return { ...state, toasts }
    }
    case "REMOVE_TOAST": {
      const toastId = action.toastId
      return {
        ...state,
        toasts: toastId ? state.toasts.filter((t) => t.id !== toastId) : [],
      }
    }
    default:
      return state
  }
}

const listeners: Array<(state: ToastState) => void> = []
let memoryState: ToastState = { toasts: [] }

function dispatch(action: ToastAction) {
  memoryState = reducer(memoryState, action)
  listeners.forEach((l) => l(memoryState))
}

type ToastInput = Omit<ToastProps, "id">

function toast(props: ToastInput) {
  const id = genId()

  const dismiss = () => dispatch({ type: "DISMISS_TOAST", toastId: id })

  dispatch({
    type: "ADD_TOAST",
    toast: {
      ...props,
      id,
      duration: props.duration ?? 4000,
    },
  })

  window.setTimeout(() => {
    dispatch({ type: "REMOVE_TOAST", toastId: id })
  }, (props.duration ?? 4000) + TOAST_REMOVE_DELAY)

  return { id, dismiss }
}

function useToast() {
  const [state, setState] = React.useState<ToastState>(memoryState)

  React.useEffect(() => {
    listeners.push(setState)
    return () => {
      const index = listeners.indexOf(setState)
      if (index > -1) listeners.splice(index, 1)
    }
  }, [])

  return {
    ...state,
    toast,
    dismiss: (toastId?: string) => dispatch({ type: "DISMISS_TOAST", toastId }),
  }
}

export { useToast, toast }
