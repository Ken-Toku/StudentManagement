"use client"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Card } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { useRouter } from "next/navigation"


type StatusType = "仮申込" | "本申込" | "受講中" | "受講終了"

export interface StudentRow {
  id: string
  name: string
  email: string
  course: string
  status: StatusType
}

const getStatusColor = (status: StatusType) => {
  switch (status) {
    case "仮申込":
      return "bg-muted text-muted-foreground"
    case "本申込":
      return "bg-chart-2/15 text-chart-2"
    case "受講中":
      return "bg-chart-1/15 text-chart-1"
    case "受講終了":
      return "bg-secondary text-secondary-foreground"
    default:
      return "bg-muted text-muted-foreground"
  }
}

export function StudentTable({ students }: { students: StudentRow[] }) {
  const router = useRouter()

  const handleRowClick = (studentId: string) => {
    router.push(`/students/${studentId}`)
  }

  return (
    <Card className="overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow className="bg-muted/50 hover:bg-muted/50">
            <TableHead className="font-semibold">名前</TableHead>
            <TableHead className="font-semibold">メールアドレス</TableHead>
            <TableHead className="font-semibold">受講コース名</TableHead>
            <TableHead className="font-semibold">申込ステータス</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {students.map((student) => (
            <TableRow
              key={student.id}
              onClick={() => handleRowClick(student.id)}
              className="cursor-pointer transition-colors hover:bg-muted/50"
            >
              <TableCell className="font-medium">{student.name}</TableCell>
              <TableCell className="text-muted-foreground">{student.email}</TableCell>
              <TableCell>{student.course}</TableCell>
              <TableCell>
                <Badge variant="secondary" className={getStatusColor(student.status)}>
                  {student.status}
                </Badge>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Card>
  )
}
