import { NextResponse } from "next/server"

export async function GET(
  _req: Request,
  ctx: { params: Promise<{ id: string }> }
) {
  const { id } = await ctx.params

  const baseUrl =
    process.env.API_BASE_URL ?? process.env.NEXT_PUBLIC_API_BASE_URL

  if (!baseUrl) {
    return NextResponse.json(
      { message: "API_BASE_URL is not set" },
      { status: 500 }
    )
  }

  const url = `${baseUrl}/student/${encodeURIComponent(id)}`
  const res = await fetch(url, { cache: "no-store" })

  const body = await res.text()
  return new NextResponse(body, {
    status: res.status,
    headers: {
      "content-type": res.headers.get("content-type") ?? "application/json",
    },
  })
}
