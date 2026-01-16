import { NextResponse } from "next/server"

export async function PUT(req: Request) {
  const baseUrl =
    process.env.API_BASE_URL ?? process.env.NEXT_PUBLIC_API_BASE_URL

  if (!baseUrl) {
    return NextResponse.json(
      { message: "API_BASE_URL is not set" },
      { status: 500 }
    )
  }

  const body = await req.text()

  const res = await fetch(`${baseUrl}/updateStudent`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body,
  })

  const text = await res.text()
  return new NextResponse(text, {
    status: res.status,
    headers: {
      "content-type": res.headers.get("content-type") ?? "application/json",
    },
  })
}
