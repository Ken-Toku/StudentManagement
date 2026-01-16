import * as React from "react"

const MOBILE_BREAKPOINT = 768

export function useIsMobile() {
  const [isMobile, setIsMobile] = React.useState<boolean>(false)

  React.useEffect(() => {
    const mediaQuery = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT - 1}px)`)

    const onChange = () => {
      setIsMobile(mediaQuery.matches)
    }

    onChange()

    if (mediaQuery.addEventListener) {
      mediaQuery.addEventListener("change", onChange)
      return () => mediaQuery.removeEventListener("change", onChange)
    } else {
      // Safari 対応（古い型）
     mediaQuery.addListener(onChange)
     return () => mediaQuery.removeListener(onChange)
    }
  }, [])

  return isMobile
}
